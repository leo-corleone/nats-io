package nats.lite.resolver;

import nats.lite.annotation.PathSubject;
import io.nats.client.impl.NatsMessage;

import java.lang.reflect.Parameter;

/**
 * @author: suen
 * @time: 2023/7/29
 * @description:
 **/
public class PathSubjectParameterResolver implements MethodParameterResolver{

    @Override
    public boolean match(Parameter parameter) {
        return parameter.isAnnotationPresent(PathSubject.class);
    }

    @Override
    public void assembleMessage(NatsMessage.Builder builder, Parameter parameter, Object arg) {
        PathSubject pathSubject = parameter.getAnnotation(PathSubject.class);
        String placeholder = pathSubject.placeholder();
        if (placeholder == null || "".equals(placeholder.trim())){
            placeholder = parameter.getName();
        }
        NatsMessage message = builder.build();
        String subject = message.getSubject();
        subject = subject.replace("{" +placeholder + "}" , String.valueOf(arg));
        builder.subject(subject);
    }
}
