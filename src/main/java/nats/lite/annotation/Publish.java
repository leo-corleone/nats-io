package nats.lite.annotation;

import java.lang.annotation.*;

/**
 * @author SunDeZhi
 * @time 2023/7/28
 * @description :
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Publish {

    String topic() default "";

    boolean isJetStreamPublish() default false;

    boolean log() default true;

}
