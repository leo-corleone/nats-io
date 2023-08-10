package nats.lite.support;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import nats.lite.annotation.NatsListener;
import nats.lite.annotation.Subscribe;
import nats.lite.configuration.NatsConfigurationTranslator;
import nats.lite.enums.ClientType;
import nats.lite.enums.Mode;
import io.nats.client.*;
import io.nats.client.api.*;
import io.nats.client.impl.NatsMessage;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.DisposableBean;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @time: 2022/9/20
 * @author: .sun
 */

@Slf4j
public abstract class AbstractNatsLiteHandler implements NatsLiteHandler, DisposableBean {

    protected ClientType clientType;

    protected Connection nc;

    protected Dispatcher dispatcher;

    private final Map<String, Method> consumerMap = new HashMap<>();

    private final Map<String, Subscription> subsList = new HashMap<>();

    protected JetStream js;


    private final Duration drainTimeout = Duration.ofSeconds(20);

    private final Duration requestTimeout = Duration.ofSeconds(20);

    public void init(NatsConfigurationTranslator translator) {
        this.nc = translator.getConnection();
        this.dispatcher = nc.createDispatcher();
        this.js = translator.getJetStream();
        subscribe();
    }

    /**
     * 用于nats初始化连接
     *
     * @param host 主机
     * @param port 端口号
     */
    @Override
    public void init(String host, Integer port) {
        init(new NatsConfigurationTranslator(host, port));
    }


    /**
     * 判断是否是连接的状态
     *
     * @return true:是 false:否
     */
    @Override
    public boolean isActive() {
        return nc != null && nc.getStatus() == Connection.Status.CONNECTED;
    }

    /**
     * @param topic 取消订阅的主题
     */
    @Override
    public void unsubscribe(String topic) {
        Subscription subscription = subsList.get(topic);
        if (subscription != null) {
            dispatcher.unsubscribe(subscription);
        }
    }

    @Override
    public void publish(String subject, Object data) {
        String dataStr = JSONObject.toJSONString(data);
        Message message = new NatsMessage.Builder().subject(subject).data(dataStr).build();
        publish(message);
    }


    @Override
    public PublishAck publishWithJs(Message message) {
        try {
            return js.publish(message);
        } catch (IOException | JetStreamApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Message request(Message message , long ms) {
        Message msg = null;
        try {
            msg = nc.requestWithTimeout(message ,Duration.ofMillis(ms)).get();;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return msg;
    }

    /**
     * 带回复主题的发布方法
     *
     * @param topic      发布主题
     * @param replyTopic 回复主题 , 这个需要发布者自己订阅
     * @param data       发布数据
     */
    @Override
    public void publish(String topic, String replyTopic, Object data) {
        Message message = new NatsMessage.Builder()
                                        .subject(topic)
                                        .replyTo(replyTopic)
                                        .data(JSONObject.toJSONString(data)).build();
        publish(message);
    }

    @Override
    public void publish(String subject, byte[] data) {
        Message message = new NatsMessage.Builder().subject(subject).data(data).build();
        publish(message);
    }

    @Override
    public void publish(Message message) {
        try {
            nc.publish(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过使用JetStream发布消息
     *
     * @param topic 发布的主题
     * @param data  发布的数据
     */
    @Override
    public PublishAck publishWithJs(String topic, Object data) {
        Message message = new NatsMessage.Builder().subject(topic).data(JSONObject.toJSONString(data)).build();
        try {
            return js.publish(message);
        } catch (IOException | JetStreamApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T request(String subject, Object payload, Class<T> clazz) {
        return request(subject, payload, requestTimeout, clazz);
    }

    public <T> T request(String subject, Object payload, Duration duration, Class<T> clazz) {
        Message message;
        try {

            log.debug("client type [{}] start nats request , subject:{}  parameters:{}", clientType, subject, payload);
            message = nc.request(subject, JSON.toJSONBytes(payload), duration);
            String result = new String(message.getData());
            return JSONObject.parseObject(result, clazz);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    public <T> T request(String subject, Object payload, Duration duration, TypeReference<T> clazz) {
        Message message;
        try {
            message = nc.request(subject, JSON.toJSONBytes(payload), duration);
            String result = new String(message.getData());
            return JSONObject.parseObject(result, clazz);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param topic 主题
     * @param data  数据
     * @param clazz 返回数据的Class类型
     */
    @Override
    public <T> T request(String topic, Object data, TypeReference<T> clazz) {
        return request(topic, data, requestTimeout, clazz);
    }

    /**
     * 普通模式订阅
     */
    protected void subscribe(Method method, Object bean) {
        Subscribe subscribe = method.getAnnotation(Subscribe.class);
        String topic = subscribe.topic();
        String queue = subscribe.queue();
        MessageHandler handler = (message) -> handleMessage(method, bean, message);
        if (queue.length() > 0) {
            subscribe(topic, queue, handler);
        } else {
            subscribe(topic, handler);
        }
    }

    public void subscribe(String subject, String queue, MessageHandler handler) {
        Subscription subscribe = dispatcher.subscribe(subject, queue, handler);
        subsList.put(subject, subscribe);
    }

    @Override
    public void subscribe(String subject, MessageHandler handler) {
        Subscription subscribe = dispatcher.subscribe(subject, handler);
        subsList.put(subject, subscribe);
    }

    /**
     * push消费模式
     *
     * @param method 反射类的订阅的方法
     * @param bean   反射类
     */
    private void consumerSubscribe(Method method, Object bean) {
        if (js == null) {
            log.error("not support JetStream configuration");
            return;
        }
        Subscribe subscribe = method.getDeclaredAnnotation(Subscribe.class);
        ConsumerConfiguration configuration = configuration(method);
        String streamName = subscribe.streamName();
        try {
            if (subscribe.mode() == Mode.PULL) {
                PullSubscribeOptions.Builder builder = new PullSubscribeOptions.Builder();
                PullSubscribeOptions options = builder.stream(streamName).configuration(configuration).build();
                JetStreamSubscription subscription = js.subscribe(subscribe.topic(), options);
                ThreadUtil.execAsync(() -> {
                    for (; ; ) {
                        List<Message> messages = subscription.fetch(1, subscribe.ackWait());
                        messages.forEach((msg) -> handleMessage(method, bean, msg));
                    }
                });
            } else if (subscribe.mode() == Mode.PUSH) {
                PushSubscribeOptions.Builder builder = new PushSubscribeOptions.Builder();
                PushSubscribeOptions options = builder.stream(streamName).configuration(configuration).build();
                js.subscribe(subscribe.topic(), dispatcher, (message) -> handleMessage(method, bean, message), subscribe.autoAck(), options);
            }
        } catch (IOException | JetStreamApiException e) {
            throw new RuntimeException(e);
        }
    }

    private ConsumerConfiguration configuration(Method method) {

        Subscribe subscribe = method.getDeclaredAnnotation(Subscribe.class);
        Mode mode = subscribe.mode();
        String durable = String.format("@Consumer@%s#%s", mode, NUID.nextGlobal());
        consumerMap.put(durable, method);
        return new ConsumerConfiguration.Builder().deliverPolicy(DeliverPolicy.All).ackPolicy(AckPolicy.Explicit).ackWait(Duration.ofSeconds(subscribe.ackWait())).durable(durable).replayPolicy(ReplayPolicy.Instant).build();
    }

    protected void handleMessage(Method method, Object bean, Message message) {
        Subscribe subscribe = method.getAnnotation(Subscribe.class);
        String topic = subscribe.topic();
        try {
            String response = new String(message.getData(), StandardCharsets.UTF_8);
            boolean isLog = subscribe.log();
            if (isLog) {
                if (subscribe.mode() == Mode.SIMPLE) {
                    log.info("subject: {} ,  received data: {}", message.getSubject(), response);
                } else {
                    log.info("subject: {} , message seq: {} received data: {}", message.getSubject(), message.metaData().streamSequence(), response);
                }
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            List<Object> args = new ArrayList<>();
            for (Class<?> parameterType : parameterTypes) {
                if (parameterType == Message.class) {
                    args.add(message);
                } else {
                    //Object data = JSON.parseObject(response, parameterType);
                    Object data = JSONObject.parseObject(response, parameterType);
                    args.add(data);
                }
            }
            Object result = method.invoke(bean, args.toArray());
            if (subscribe.reply() && message.getReplyTo() != null) {
                publish(message.getReplyTo(), result);
            }
            if (isLog) {
                log.info("reply subject:{} , result:[{}]", message.getReplyTo() != null ? message.getReplyTo() : "no reply subject", result);
            }
            if (message.isJetStream() && subscribe.autoAck()) {
                message.ack();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (message.getReplyTo() != null) {
                publish(message.getReplyTo(), "error");
            }
            log.error("nats report appear error , method : {} , topic:{}", method.getDeclaringClass().getName() + "." + method.getName(), topic, e);
        }
    }


    protected void subscribe() {

        log.info("init subscribe");
        Reflections ref = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage("nats.lite.example")));

        Set<Class<?>> clazzs = ref.getTypesAnnotatedWith(NatsListener.class);

        for (Class<?> clazz : clazzs) {
            Method[] methods = clazz.getDeclaredMethods();
            Object bean = SpringUtil.getBean(clazz);
            NatsListener listener = clazz.getDeclaredAnnotation(NatsListener.class);
            if (listener.value() == clientType) {
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(Subscribe.class)) {
                        continue;
                    }
                    Subscribe subscribe = method.getAnnotation(Subscribe.class);
                    method.setAccessible(true);
                    if (subscribe.mode() == Mode.SIMPLE) {
                        subscribe(method, bean);
                    } else {
                        consumerSubscribe(method, bean);
                    }
                }
            }
        }
    }


    private void consumerClear() throws IOException {
        if (!consumerMap.isEmpty()) {
            JetStreamManagement js = nc.jetStreamManagement();
            consumerMap.forEach((durable, method) -> {
                try {
                    Subscribe subscribe = method.getDeclaredAnnotation(Subscribe.class);
                    String streamName = subscribe.streamName();
                    ConsumerInfo consumerInfo = js.getConsumerInfo(streamName, durable);
                    if (consumerInfo != null) {
                        js.deleteConsumer(streamName, durable);
                    }
                } catch (IOException | JetStreamApiException ignored) {
                }
            });
        }
        consumerMap.clear();
    }


    /**
     * @return 获取客户端类型
     */
    @Override
    public ClientType getClientType() {
        return this.clientType;
    }


    @Override
    public void destroy() {
        try {
            if (isActive()) {
                consumerClear();
                nc.drain(drainTimeout).get();
            }
        } catch (Exception e) {
            log.error("nats fail to destroy");
        }
    }
}
