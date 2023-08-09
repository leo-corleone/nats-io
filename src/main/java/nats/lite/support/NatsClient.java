package nats.lite.support;

import com.alibaba.fastjson.TypeReference;
import io.nats.client.*;
import io.nats.client.api.PublishAck;
import lombok.extern.slf4j.Slf4j;

/**
 * @author .sun
 * @date 2022/8/14
 **/

@Slf4j
public class NatsClient {

    private NatsManager manager;

    public NatsClient() {
    }

    public void setManager(NatsManager manager) {
        this.manager = manager;
    }

    public void subscribe(String topic, MessageHandler handler) {
        manager.subscribe(topic, handler);
    }

    public void unsubscribe(String topic) {
        manager.unsubscribe(topic);
    }

    public void publish(String topic, Object data) {
        manager.publish(topic, data);
    }


    public void publish(String topic , String replyTopic, Object data) {
        manager.publish(topic , replyTopic, data);
    }

    public void publish(String topic, byte[] data) {
        manager.publish(topic, data);
    }

    public void publishWithLog(String topic, Object data) {
        log.info("[Publish]  subject:{} , data:{}", topic, data);
        publish(topic, data);
    }

    public PublishAck publishWithJs(String topic, Object data) {
        return manager.publishWithJs(topic, data);
    }

    public <T> T request(String topic, Object data, Class<T> clazz) {
        return manager.request(topic, data, clazz);
    }


    public <T> T request(String topic, Object data, TypeReference<T> clazz) {
        return manager.request(topic, data, clazz);
    }


}
