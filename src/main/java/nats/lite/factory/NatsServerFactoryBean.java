package nats.lite.factory;

import nats.lite.proxy.DefaultTarget;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author SunDeZhi
 * @time 2023/7/28
 * @description :
 */
public class NatsServerFactoryBean implements FactoryBean<Object> {


    private Class<?> proxyObject;


    private <T> T getTarget(){
        return new DefaultTarget().target(proxyObject);
    }


    public void setProxyObject(Class<?> proxyObject) {
        this.proxyObject = proxyObject;
    }


    @Override
    public Object getObject()  {
        return getTarget();
    }

    @Override
    public Class<?> getObjectType() {
        return proxyObject;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
