package nats.lite.resolver;

import nats.lite.annotation.Header;
import io.nats.client.impl.Headers;
import io.nats.client.impl.NatsMessage;

import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * @author: suen
 * @time: 2023/7/29
 * @description:
 **/
public class HeadersParameterResolver implements MethodParameterResolver{
    @Override
    public boolean match(Parameter parameter) {
        return parameter.isAnnotationPresent(Header.class);
    }

    @Override
    public void assembleMessage(NatsMessage.Builder builder, Parameter parameter, Object arg) {
        Headers headers = new Headers();
        if (arg instanceof Map){
            Map<String , Object> map = (Map)arg;
            map.forEach((k,v)->{
                headers.add(k , v.toString());
            });
        }else {
            Header header = parameter.getAnnotation(Header.class);
            headers.add(header.name() , arg.toString());
        }
    }
}
