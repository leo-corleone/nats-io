package nats.lite.annotation;

import nats.lite.register.NatsServerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author SunDeZhi
 * @time 2023/7/28
 * @description :
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(NatsServerRegistrar.class)
public @interface EnableNatsDiscovery {


    String[] basePackages() default {};

}
