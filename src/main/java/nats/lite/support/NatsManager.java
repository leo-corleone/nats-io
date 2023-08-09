package nats.lite.support;

import com.alibaba.fastjson.TypeReference;
import io.nats.client.MessageHandler;
import io.nats.client.api.PublishAck;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @time: 2022/9/20
 * @author: .sun
 */
@Component
public class NatsManager {


    @Lazy
    @Resource
    private List<NatsHandler> handlers;


    public void addNatsHandler(List<NatsHandler> natsHandlers) {
        handlers.addAll(natsHandlers);
    }


    public void subscribe(String topic, MessageHandler handler) {
        for (NatsHandler natsHandler : handlers) {
            if (natsHandler.match(topic) && natsHandler.isActive()) {
                natsHandler.subscribe(topic, handler);
            }
        }
    }


    public void unsubscribe(String topic) {
        for (NatsHandler natsHandler : handlers) {
            if (natsHandler.match(topic) && natsHandler.isActive()) {
                natsHandler.unsubscribe(topic);
            }
        }
    }

    public void publish(String topic, Object data) {
        for (NatsHandler natsHandler : handlers) {
            if (natsHandler.match(topic) && natsHandler.isActive()) {
                natsHandler.publish(topic, data);
            }
        }
    }


    public void publish(String topic, byte[] data) {
        for (NatsHandler natsHandler : handlers) {
            if (natsHandler.match(topic) && natsHandler.isActive()) {
                natsHandler.publish(topic, data);
            }
        }
    }


    public void publish(String topic , String replyTopic, Object data) {
        for (NatsHandler natsHandler : handlers) {
            if (natsHandler.match(topic) && natsHandler.isActive()) {
                natsHandler.publish(topic ,replyTopic, data);
            }
        }
    }

    public PublishAck publishWithJs(String topic, Object data) {
        for (NatsHandler natsHandler : handlers) {
            if (natsHandler.match(topic) && natsHandler.isActive()) {
                return natsHandler.publishWithJs(topic, data);
            }
        }
        return null;
    }

    public <T> T request(String topic, Object data, Class<T> clazz) {
        for (NatsHandler natsHandler : handlers) {
            if (natsHandler.match(topic) && natsHandler.isActive()) {
                return natsHandler.request(topic, data, clazz);
            }
        }
        return null;
    }


    public <T> T request(String topic, Object data, TypeReference<T> clazz) {
        for (NatsHandler natsHandler : handlers) {
            if (natsHandler.match(topic) && natsHandler.isActive()) {
                return natsHandler.request(topic, data, clazz);
            }
        }
        return null;
    }


}
