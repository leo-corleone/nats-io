package nats.lite.example.startup;

import nats.lite.example.service.NatsLiteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: suen
 * @time: 2023/8/9
 * @description:
 **/

@Service
public class StartupNatsLite implements CommandLineRunner {

    @Resource
    private NatsLiteService natsLiteService;


    @Override
    public void run(String... args) throws Exception {
        natsLiteService.publish();
        natsLiteService.request();
    }
}
