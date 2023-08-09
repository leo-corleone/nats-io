package nats.lite.annotation;

import java.lang.annotation.*;

/**
 * @author: suen
 * @time: 2023/7/30
 * @description:
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface ReplyTo {
}
