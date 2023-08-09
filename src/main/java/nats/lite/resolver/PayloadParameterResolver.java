package nats.lite.resolver;

import com.alibaba.fastjson.JSONObject;
import nats.lite.annotation.Payload;
import io.nats.client.impl.NatsMessage;

import java.lang.reflect.Parameter;

/**
 * @author: suen
 * @time: 2023/7/29
 * @description:
 **/
public class PayloadParameterResolver implements MethodParameterResolver{
    @Override
    public boolean match(Parameter parameter) {
        return parameter.isAnnotationPresent(Payload.class);
    }

    @Override
    public void assembleMessage(NatsMessage.Builder builder, Parameter parameter, Object arg) {
          builder.data(JSONObject.toJSONBytes(arg));
    }
}
