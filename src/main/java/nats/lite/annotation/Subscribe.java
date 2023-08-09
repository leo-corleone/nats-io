package nats.lite.annotation;



import nats.lite.enums.Mode;

import java.lang.annotation.*;

/**
 * @author .sun
 * @date 2022/8/10
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface Subscribe {

    /**
     *  nats 主题
     */
    String topic();

    /**
     * 队列名称
     */
    String queue() default "";

    /**
     * 回复
     * 是否将方法的结果回复，
     *   1. true: 方法必须有返回结果 ,必须是请求-回复的模式
     */
    boolean reply() default false;

    /**
     * 日志
     * 收到消息时，是否打印订阅信息
     */
    boolean log() default false;

    /**
     * 模式
     * 目前有三种模式:
     *    SIMPLE(默认)，
     *    PUSH(在开启JetStream情况下使用)，
     *    PULL(不建议使用)
     *
     */
    Mode mode() default Mode.SIMPLE;

    /**
     * 自动确认
     *  只有在PUSH和PULL模式下才生效
     *  true: 默认为收到消息自动确认:
     *  false: 收到消息后必须自己确认，否则消息将会一直发送，知道确认为止
     *  确认操作:
     *     ack: 消息正常确认
     *     nak: 不确认，消息不再发送
     *     term: 消息不需要再继续发送
     *     inProgress: 消息在处理中，稍后再确认
     */
    boolean autoAck() default true;

    /**
     * 消息确认等待时间，超过这个时间
     *  默认: 5s
     *  自定义: 1秒 --> 1s
     *         1分 --> 1m
     */
    long ackWait() default 5;


    /**
     * 当nats服务中只有一个Stream配置，且streamName未指定，则直接使用Stream
     *  若有多个Stream配置，未指定streamName则会出现异常
     */
    String streamName() default "";


}

