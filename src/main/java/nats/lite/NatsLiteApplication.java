package nats.lite;

import nats.lite.annotation.EnableNatsDiscovery;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author: suen
 * @time: 2023/7/27
 * @description:
 **/

@EnableNatsDiscovery(basePackages = "nats.lite.example.api")
@SpringBootApplication
public class NatsLiteApplication {


    public static void main(String[] args) {
        SpringApplication.run(NatsLiteApplication.class, args);
        System.out.println("Nats lite finish to startup");
    }

}
