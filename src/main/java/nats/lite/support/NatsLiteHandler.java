package nats.lite.support;

import com.alibaba.fastjson.TypeReference;
import nats.lite.enums.ClientType;
import io.nats.client.Message;
import io.nats.client.MessageHandler;
import io.nats.client.api.PublishAck;

/**
 * @time: 2022/9/20
 * @author: .sun
 */
public interface NatsLiteHandler {


    /**
     * 用于nats初始化连接
     * @param host 主机
     * @param port 端口号
     */
    void init(String host , Integer port);


    /**
     * 用于nats初始化连接
     * @param host 主机
     * @param port 端口号
     * @param username 用户名
     * @param password 密码
     */
    default  void init(String host , Integer port , String username , String password){};

    /**
     *  支持的平台客户端
     *
     * @param subject 使用主题匹配
     */
    boolean match(String subject);


    /**
     * 获取客户端类型
     * @return 客户端类型
     */
    ClientType getClientType();

    /**
     *
     * 仅支持异步订阅
     * @param topic 订阅的主题
     * @param handler 消息异步处理器
     */
    void subscribe(String topic , MessageHandler handler);


    /**
     *
     * @param topic 取消订阅的主题
     */
    void unsubscribe(String topic);

    /**
     *
     * @param topic 发布的主题
     * @param data 发布的数据
     */
    void publish(String topic , Object data);


    PublishAck publishWithJs(Message message);


    void publish(Message message);

    /**
     *
     * @param topic 发布的主题
     * @param data 发布的数据
     */
    void publish(String topic , byte[] data);



    Message request(Message message , long second);


    /**
     * 带回复主题的发布方法
     * @param topic 发布主题
     * @param replyTopic 回复主题 , 这个需要发布者自己订阅
     * @param data 发布数据
     */
    void publish(String topic , String replyTopic , Object data);


    /**
     * 通过使用JetStream发布消息
     * @param topic 发布的主题
     * @param data 发布的数据
     */
    PublishAck publishWithJs(String topic , Object data);

    /**
     *
     * @param topic 主题
     * @param data 数据
     * @param clazz 返回数据的Class类型
     * @param <T> 返回数据的类型
     */
    <T> T request(String topic , Object data , Class<T> clazz);


    /**
     *
     * @param topic 主题
     * @param data 数据
     * @param clazz 返回数据的Class类型
     * @param <T> 返回数据的类型
     */
    <T> T request(String topic , Object data , TypeReference<T> clazz);


    /**
     * 由于有很多地方都在调用 publish()、request() 方法,包括定时任务,
     * 当项目关闭时,定时任务仍然在执行，
     * 而此时的nats已经被销毁关闭连接，再去调用就会出现异常
     * @return 判断当前处理器是否允许调用
     */
    boolean isActive();


    /**
     * 进行连接销毁
     */
    void destroy();

}
