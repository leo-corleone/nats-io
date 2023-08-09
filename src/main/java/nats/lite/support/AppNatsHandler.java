package nats.lite.support;


import nats.lite.enums.ClientType;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 *
 * 业务的nats客户端
 * @author .sun
 * @date 2022/1/15
 **/

@Order(Ordered.HIGHEST_PRECEDENCE)
public class AppNatsHandler extends AbstractNatsHandler {

    public AppNatsHandler() {
       clientType = ClientType.APPLICATION;
    }

    @Override
    public boolean match(String subject) {
        return subject.contains("wjzq");
    }

}

