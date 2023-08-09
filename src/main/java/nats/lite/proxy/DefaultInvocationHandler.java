package nats.lite.proxy;

import nats.lite.enums.ClientType;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author SunDeZhi
 * @time 2023/7/28
 * @description :
 */
public class DefaultInvocationHandler implements InvocationHandler {


    private final NatsProxy proxy;

    public DefaultInvocationHandler(ClientType clientType) {
        this.proxy = new NatsProxy(clientType);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        if ("equals".equals(method.getName())) {
            try {
                Object otherHandler =
                        args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
                return equals(otherHandler);
            } catch (IllegalArgumentException e) {
                return false;
            }
        } else if ("hashCode".equals(method.getName())) {
            return hashCode();
        } else if ("toString".equals(method.getName())) {
            return toString();
        }
        return this.proxy.invoke(method , args);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DefaultInvocationHandler) {
            DefaultInvocationHandler other = (DefaultInvocationHandler) obj;
            return super.equals(other);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
