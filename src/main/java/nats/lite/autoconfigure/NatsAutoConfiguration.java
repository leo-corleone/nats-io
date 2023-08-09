package nats.lite.autoconfigure;

import cn.hutool.core.util.ObjectUtil;
import nats.lite.configuration.NatsConfiguration;
import nats.lite.configuration.NatsConfigurationTranslator;
import nats.lite.configuration.NatsConnectionFactory;
import nats.lite.support.AppNatsHandler;
import nats.lite.support.NatsClient;
import nats.lite.support.NatsManager;
import nats.lite.js.JetStreamBuilder;
import nats.lite.listener.NatsConnectionListener;
import nats.lite.listener.NatsErrorListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author .sun
 * @date 2022/1/14
 **/
@Slf4j
@Configuration
@EnableConfigurationProperties(NatsProperties.class)
public class NatsAutoConfiguration {

    private final NatsProperties properties;


    public NatsAutoConfiguration(NatsProperties properties) {
        this.properties = properties;
    }


    @ConditionalOnMissingBean
    @Bean
    public NatsConfiguration natsConfiguration() {
        NatsConnectionFactory factory = new NatsConnectionFactory();
        factory.setConfigurationProperties(properties);
        factory.setConnectionListener(new NatsConnectionListener());
        factory.setErrorListener(new NatsErrorListener());
        return factory.getObject();
    }



    @Bean
    @ConditionalOnProperty(value = "spring.nats.enable" , havingValue = "true")
    public AppNatsHandler appNatsHandler(NatsConfiguration configuration , List<JetStreamBuilder> jetStreamBuilders){

        AppNatsHandler natsHandler = new AppNatsHandler();
        if (ObjectUtil.isNotEmpty(jetStreamBuilders)){
            natsHandler.init(new NatsConfigurationTranslator(configuration ,jetStreamBuilders));
        }else {
            natsHandler.init(new NatsConfigurationTranslator(configuration));
        }
        return natsHandler;
    }



    @Bean
    public NatsClient natsClient(NatsManager manager){
        NatsClient natsClient = new NatsClient();
        natsClient.setManager(manager);
        return natsClient;
    }




}
