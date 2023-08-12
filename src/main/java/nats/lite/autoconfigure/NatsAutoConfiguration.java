package nats.lite.autoconfigure;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import nats.lite.configuration.NatsConfiguration;
import nats.lite.configuration.NatsConfigurationTranslator;
import nats.lite.configuration.NatsConnectionFactory;
import nats.lite.factory.NatsLiteFactoryBean;
import nats.lite.js.JetStreamBuilder;
import nats.lite.listener.NatsConnectionListener;
import nats.lite.listener.NatsErrorListener;
import nats.lite.proxy.NatsProxy;
import nats.lite.support.*;
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
    @ConditionalOnProperty(value = "spring.nats.enable", havingValue = "true")
    public NatsLiteHandler applicationNatsLite(NatsConfiguration configuration, List<JetStreamBuilder> jetStreamBuilders) {
        AppNatsLiteHandler natsHandler = new AppNatsLiteHandler();
        if (ObjectUtil.isNotEmpty(jetStreamBuilders)) {
            natsHandler.init(new NatsConfigurationTranslator(configuration, jetStreamBuilders));
        } else {
            natsHandler.init(new NatsConfigurationTranslator(configuration));
        }
        return natsHandler;
    }


    @Bean
    public NatsLiteHandler midisNatsLite() {
        MidisNatsLiteHandler midisNatsLiteHandler = new MidisNatsLiteHandler();
        NatsLiteFactoryBean factoryBean = new NatsLiteFactoryBean();
        factoryBean.setHandler(midisNatsLiteHandler);
        return factoryBean.getObject();
    }


    @Bean
    public NatsLiteHandler cloudMidisNatsLite() {
        CloudMidisNatsLiteHandler cloudMidisNatsLiteHandler = new CloudMidisNatsLiteHandler();
        NatsLiteFactoryBean factoryBean = new NatsLiteFactoryBean();
        factoryBean.setHandler(cloudMidisNatsLiteHandler);
        return factoryBean.getObject();
    }


    @Bean
    public NatsLiteHandler audioNatsLite() {
        AudioLineNatsLiteHandler audioLineNatsLiteHandler = new AudioLineNatsLiteHandler();
        NatsLiteFactoryBean factoryBean = new NatsLiteFactoryBean();
        factoryBean.setHandler(audioLineNatsLiteHandler);
        return factoryBean.getObject();
    }


}
