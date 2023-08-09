package nats.lite.resolver;

import nats.lite.annotation.ReplyTo;
import io.nats.client.impl.NatsMessage;

import java.lang.reflect.Parameter;

/**
 * @author: suen
 * @time: 2023/7/30
 * @description:
 **/
public class ReplySubjectParameterResolver implements MethodParameterResolver {
    @Override
    public boolean match(Parameter parameter) {
        return parameter.isAnnotationPresent(ReplyTo.class);
    }

    @Override
    public void assembleMessage(NatsMessage.Builder builder, Parameter parameter, Object arg) {
        if (arg == null || arg.toString().equals("")) {
            return;
        }
        builder.replyTo(arg.toString());
    }
}