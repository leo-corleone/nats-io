package nats.lite.example.service;

import nats.lite.annotation.NatsListener;
import nats.lite.annotation.Subscribe;
import io.nats.client.Message;
import org.springframework.stereotype.Component;

/**
 * @author: suen
 * @time: 2023/7/30
 * @description:
 **/

@Component
@NatsListener
public class NatsLiteSubscription {

    @Subscribe(topic = "$request.get.data.nats.proxy" , reply = true , log = true)
    public String subscribe(String str , Message message){
        return "1231212";
    }

    @Subscribe(topic = "$request.get.data.nats.*" , reply = true , log = true)
    public String subscribeWarcaild(String str , Message message){
        return "123178789878212";
    }

}
