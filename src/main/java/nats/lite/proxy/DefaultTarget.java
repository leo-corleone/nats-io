package nats.lite.proxy;

import nats.lite.annotation.NatsServer;

import java.lang.reflect.Proxy;

/**
 * @author SunDeZhi
 * @time 2023/7/28
 * @description :
 */
final public class DefaultTarget implements Targeter{


    /**
     * 获取反射对应的实例化类
     *
     * @param clazz 反射类
     * @return 实例化
     */
    @Override
    public <T> T target(Class<?> clazz) {
        if (clazz.isAnnotationPresent(NatsServer.class)){
            NatsServer natsServer = clazz.getAnnotation(NatsServer.class);
            Object target = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new DefaultInvocationHandler(natsServer.proxyClient()));
            return (T)target;
        }
        return null;
    }
}
