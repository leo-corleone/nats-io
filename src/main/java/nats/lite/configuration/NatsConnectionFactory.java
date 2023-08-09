package nats.lite.configuration;

import nats.lite.autoconfigure.NatsProperties;
import io.nats.client.AuthHandler;
import io.nats.client.ConnectionListener;
import io.nats.client.ErrorListener;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.time.Duration;


/**
 * @time: 2022/10/24
 * @author: .sun
 */
public class NatsConnectionFactory implements FactoryBean<NatsConfiguration> , InitializingBean {


    private NatsProperties properties = new NatsProperties();

    private NatsConfiguration configuration;

    private ConnectionListener connectionListener;

    private ErrorListener errorListener;

    private AuthHandler authHandler;


    public void setConfigurationProperties(NatsProperties natsProperties){
        this.properties = natsProperties;
    }

    public void setConnectionListener(ConnectionListener listener){
        this.connectionListener = listener;
    }

    public void setErrorListener(ErrorListener listener){
        this.errorListener = listener;
    }

    public void setHandler(AuthHandler handler) {
        this.authHandler = handler;
    }

    @Override
    public NatsConfiguration getObject()  {

        if (this.configuration == null){
            afterPropertiesSet();
        }
        return this.configuration;
    }


    @Override
    public Class<?> getObjectType() {
        return this.configuration == null ? NatsConfiguration.class : this.configuration.getClass();
    }


    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet()  {

         configuration = new NatsConfiguration();
         configuration.setHost(properties.getHost());
         configuration.setPort(properties.getPort());
         configuration.setConnectionName(properties.getConnectionName());
         configuration.setConnectionTimeout(properties.getConnectionTimeout());
         configuration.setBufferSize(properties.getBufferSize());
         configuration.setDrainTimeout(properties.getDrainTimeout());
         configuration.setMaxReconnects(properties.getMaxReconnects());
         configuration.setEnableJetStream(properties.getEnableJetStream());
         configuration.setNoEcho(properties.getNoEcho());
         configuration.setPingInterval(Duration.ofSeconds(properties.getPingInterval()));
         configuration.setUtfSupport(properties.getUtf8Support());
         configuration.setNoHeaders(properties.getNoHeaders());
         configuration.setNoReconnect(properties.getNoReconnect());
         configuration.setMaxPingOut(properties.getMaxPingsOut());
         configuration.setReconnectWait(properties.getReconnectWait());
         configuration.setRequestTimeout(properties.getRequestTimeout());
         configuration.setUsername(properties.getUsername());
         configuration.setPassword(properties.getPassword());
         configuration.setToken(properties.getToken());

         if (connectionListener != null){
            configuration.setConnectionListener(connectionListener);
         }
         if (errorListener != null){
            configuration.setErrorListener(errorListener);
         }
         if (authHandler != null){
            configuration.setAuthHandler(authHandler);
         }
         configuration.setTrace(properties.getTrace());

    }
}
