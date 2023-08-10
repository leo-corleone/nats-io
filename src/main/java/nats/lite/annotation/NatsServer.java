package nats.lite.annotation;

import nats.lite.enums.ClientType;

import java.lang.annotation.*;

/**
 * nats 业务监听注解
 * @author .sun
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NatsServer {


    /**
     * 监听器类型（APPLICATION：业务，CloudMidis：中台 , Midis: ）
     */
    ClientType proxyClient() default ClientType.Application;
}
