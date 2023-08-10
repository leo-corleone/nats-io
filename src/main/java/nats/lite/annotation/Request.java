package nats.lite.annotation;

import nats.lite.resolver.DefaultReturnValueResolver;
import nats.lite.resolver.ReturnValueResolver;

import java.lang.annotation.*;

/**
 * @author SunDeZhi
 * @time 2023/7/28
 * @description :
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Request {

    String topic() default "";

    long timeout() default  1;

    boolean log() default true;

    Class<? extends ReturnValueResolver> returnResolver() default DefaultReturnValueResolver.class;
}
