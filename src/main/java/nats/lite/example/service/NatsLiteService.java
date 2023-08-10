package nats.lite.example.service;

import nats.lite.example.api.NatsCloudService;
import nats.lite.example.domain.NatsData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: suen
 * @time: 2023/7/28
 * @description:
 **/

@Service
public class NatsLiteService {


    @Resource
    private NatsCloudService natsCloudService;



    public void publish(){
        NatsData natsData = new NatsData();
        natsData.setId(1100111001);
        natsData.setName("nats_lite_project");
        natsCloudService.publish(natsData);
    }

    public void request(){
        NatsData natsData = new NatsData();
        natsData.setId(1100111001);
        natsData.setName("nats_lite_project");
        String request = natsCloudService.request( natsData);
    }


}
