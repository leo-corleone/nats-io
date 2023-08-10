package nats.lite.factory;

import nats.lite.support.NatsLiteHandler;
import org.springframework.beans.factory.*;

/**
 * @author SunDeZhi
 * @time 2023/8/10
 * @description :
 */
public class NatsLiteFactoryBean implements FactoryBean<NatsLiteHandler> {


    private NatsLiteHandler handler;


    public void setHandler(NatsLiteHandler handler){
        this.handler = handler;
    }

    @Override
    public NatsLiteHandler getObject()  {
        return handler;
    }


    @Override
    public Class<?> getObjectType() {
        return handler.getClass();
    }


    @Override
    public boolean isSingleton() {
        return true;
    }
}
