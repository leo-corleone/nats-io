package nats.lite;

import nats.lite.annotation.EnableNatsDiscovery;
import nats.lite.example.service.NatsLiteService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


/**
 * @author: suen
 * @time: 2023/7/27
 * @description:
 **/

@EnableNatsDiscovery(basePackages = "nats.lite.example.api")
@SpringBootApplication
public class App {


    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        System.out.println("Nats lite finish to startup");
    }

}
