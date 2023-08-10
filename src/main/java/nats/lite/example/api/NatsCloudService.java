package nats.lite.example.api;

import nats.lite.annotation.*;
import nats.lite.enums.ClientType;
import nats.lite.example.domain.NatsData;

/**
 * @author SunDeZhi
 * @time 2023/7/28
 * @description :
 */



@NatsServer(proxyClient = ClientType.Application)
public interface NatsCloudService {


    @Publish(topic = "$report.update.data.{project}.{module}")
    void publish(@PathSubject String project,
                 @PathSubject String module ,
                 @Payload NatsData natsData);



    @Request(topic = "$request.get.data.{project}.{module}")
    Integer request(@PathSubject String project,
                    @PathSubject String module ,
                    @Payload NatsData natsData);
}
