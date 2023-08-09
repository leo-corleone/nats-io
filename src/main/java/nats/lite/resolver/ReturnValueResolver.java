package nats.lite.resolver;

import nats.lite.enums.ClientType;
import io.nats.client.Message;

/**
 * @author SunDeZhi
 * @time 2023/7/31
 * @description : 返回参数解析器
 */
public interface ReturnValueResolver {


    boolean match(ClientType clientType);


    Object convertResult(Message message , Class<?> returnType);

}
