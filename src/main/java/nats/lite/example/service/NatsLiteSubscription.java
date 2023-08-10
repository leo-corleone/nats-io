package nats.lite.example.service;

import nats.lite.annotation.NatsListener;
import nats.lite.annotation.Subscribe;
import io.nats.client.Message;
import nats.lite.example.domain.NatsData;
import org.springframework.stereotype.Component;

/**
 * @author: suen
 * @time: 2023/7/30
 * @description:
 **/

@Component
@NatsListener
public class NatsLiteSubscription {

    @Subscribe(topic = "$report.update.data.nats.lite" , log = true)
    public void subscribePublish(NatsData natsData){
        System.out.println(natsData);
    }

    @Subscribe(topic = "$request.get.data.nats.lite" , reply = true , log = true)
    public String subscribeRequest(NatsData natsData){
        System.out.println(natsData);
        return "success";
    }

}
