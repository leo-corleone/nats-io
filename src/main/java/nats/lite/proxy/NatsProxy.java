package nats.lite.proxy;

import com.alibaba.fastjson.JSONObject;
import nats.lite.annotation.Publish;
import nats.lite.annotation.Request;
import nats.lite.resolver.*;
import nats.lite.support.NatsLiteHandler;
import nats.lite.support.NatsLiteHandlerFactory;
import nats.lite.enums.ClientType;
import io.nats.client.Message;
import io.nats.client.api.PublishAck;
import io.nats.client.impl.NatsMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SunDeZhi
 * @time 2023/7/28
 * @description :
 */
public class NatsProxy {

    private static Log LOG = LogFactory.getLog(NatsProxy.class);

    private final ClientType proxyServer;

    private static final List<MethodParameterResolver> PARAM_RESOLVERS = new ArrayList<MethodParameterResolver>(){{
        add(new PathSubjectParameterResolver());
        add(new PayloadParameterResolver());
        add(new HeadersParameterResolver());
        add(new ReplySubjectParameterResolver());
    }};

    private static final List<ReturnValueResolver> RETURN_RESOLVERS = new ArrayList<ReturnValueResolver>(){{
        add(new DefaultReturnValueResolver());
    }};

    public static void addParameterResolver(MethodParameterResolver resolver){
        PARAM_RESOLVERS.add(resolver);
    }
    public static void addReturnValueResolver(ReturnValueResolver resolver){
        RETURN_RESOLVERS.add(resolver);
    }
    public NatsProxy(ClientType proxyServer) {
        this.proxyServer = proxyServer;
    }

    public Object invoke(Method method , Object[] args){
        return dispatch(method , args);
    }

    private Object dispatch(Method method, Object[] args) {
        if (method.isAnnotationPresent(Publish.class)){
            Publish publish = method.getAnnotation(Publish.class);
            Message message = assembleMessage(publish.topic() , method , args);
            return handlePublish(message , publish);
        }
        if (method.isAnnotationPresent(Request.class)){
            Request request = method.getAnnotation(Request.class);
            Message message = assembleMessage(request.topic() , method , args);
            message = handleRequest(message, request);
            return convertorMessage(method , request , message);
        }
        throw new RuntimeException("Due to not specify a type [Request or Publish] , So can't handle the method");
    }

    private Object convertorMessage(Method method , Request request, Message message) {
        Class<? extends ReturnValueResolver> returnResolver = request.returnResolver();
        for (ReturnValueResolver resolver : RETURN_RESOLVERS) {
            if (resolver.getClass() == returnResolver && resolver.match(proxyServer)){
               return  resolver.convertResult(message , method.getReturnType());
            }
        }
        return JSONObject.parseObject(new String(message.getData()) , method.getReturnType());
    }


    private Message assembleMessage(String subject, Method method, Object[] args) {
        NatsMessage.Builder builder = new NatsMessage.Builder();
        builder.subject(subject);
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            for (MethodParameterResolver paramResolver : PARAM_RESOLVERS) {
                if (paramResolver.match(parameters[i])){
                    paramResolver.assembleMessage(builder , parameters[i] , args[i]);
                    break;
                }
            }
        }
        return builder.build();
    }

    private Object handlePublish(Message message , Publish publish) {
        if (publish.log()){
            LOG.info("PUBLISH --> ClientType:[{" + proxyServer + "}] ,data:[" + new String(message.getData()) + "]  , subject:[" +  message.getSubject() +"], " +
                    "replyTo:[" + message.getReplyTo() + "] , isJetStreamPublish:[" + publish.isJetStreamPublish() + "]");
        }
        NatsLiteHandler handler = NatsLiteHandlerFactory.getHandler(proxyServer);
        PublishAck publishAck = null;
        long currentTime = System.currentTimeMillis();
        if (publish.isJetStreamPublish()){
            publishAck = handler.publishWithJs(message);
        }else {
            handler.publish(message);
        }
        if (publish.log()){
            LOG.info("Finish Publish <-- ClientType:[" + proxyServer + "}] , subject:[" +  message.getSubject() +"], " +
                    "replyTo:[" + message.getReplyTo() + "] , isJetStreamPublish:[" + publish.isJetStreamPublish() + "] , " +
                    "PublishAck:[" + publishAck + "] , consume:[" + (System.currentTimeMillis() - currentTime) + "ms]");
        }
        return publishAck;
    }

    private Message handleRequest(Message message ,Request request) {
        if (request.log()){
            LOG.info("REQUEST --> ClientType:[{" + proxyServer + "}] ,data:[" + new String(message.getData()) + "] , subject:[" +  message.getSubject() +"], " +
                    "replyTo:[" + message.getReplyTo() + "] ,Timeout:[" + request.timeout() + "ms]");
        }
        NatsLiteHandler handler = NatsLiteHandlerFactory.getHandler(proxyServer);
        Message msg = null;
        try {
            long currentTime = System.currentTimeMillis();
            msg = handler.request(message , request.timeout());
            if (request.log()){
                LOG.info("Finish REQUEST <-- ClientType:[{" + proxyServer+ "}] , subject:[" +  message.getSubject() +"], " +
                        "replyTo:[" + message.getReplyTo() + "] , request data:[" + new String(message.getData()) + "]  , request result:[" + new String(msg.getData()) + "] , consume:[" + (System.currentTimeMillis() - currentTime)  + "ms]");
            }
        }catch (Exception e){
             LOG.error("REQUEST FAILURE  ClientType:[{" + proxyServer+ "}]  subject:[" +  message.getSubject() +"]" ,e);
             throw e;
        }
        return msg;
    }


}
