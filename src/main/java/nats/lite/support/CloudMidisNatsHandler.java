package nats.lite.support;

import nats.lite.configuration.NatsConfigurationTranslator;
import nats.lite.enums.ClientType;
import org.springframework.stereotype.Component;

/**
 *
 * 中台的nats客户端
 * @time: 2022/8/12
 * @author: .sun
 */

@Component
public class CloudMidisNatsHandler extends AbstractNatsHandler {


    private final String connectionName = "TZ_APP_CloudMidis";

    public CloudMidisNatsHandler() {
        clientType = ClientType.CloudMidis;
    }


    @Override
    public boolean match(String subject) {
        return false;
    }


    public void init(String host, Integer port) {
        // 断开之前的连接
        destroy();
        init(new NatsConfigurationTranslator(host , port ,connectionName));
    }
}
