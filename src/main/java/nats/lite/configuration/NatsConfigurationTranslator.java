package nats.lite.configuration;

import nats.lite.js.JetStreamBuilder;
import nats.lite.listener.NatsConnectionListener;
import nats.lite.listener.NatsErrorListener;
import io.nats.client.*;
import io.nats.client.api.StreamConfiguration;
import io.nats.client.api.StreamInfo;
import io.nats.client.support.NatsConstants;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

/**
 * @time: 2022/10/28
 * @author: .sun
 */
public class NatsConfigurationTranslator {

    private Connection nc;

    private JetStream js;


    public NatsConfigurationTranslator(String host){
       this(host , Options.DEFAULT_PORT);
    }

    public NatsConfigurationTranslator(String host , Integer port){
       this(host , port , null);
    }

    public NatsConfigurationTranslator(String host , Integer port , String connectionName){

        Options.Builder builder = new Options.Builder();
        builder.server(NatsConstants.NATS_PROTOCOL_SLASH_SLASH + host + ":" + port);
        builder.connectionName(connectionName);
        builder.connectionListener(new NatsConnectionListener());
        builder.errorListener(new NatsErrorListener());
        try {
            this.nc = Nats.connect(builder.build());
            this.js = nc.jetStream();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }


    public NatsConfigurationTranslator(String host , Integer port , String username , String password , String connectionName){

        Options.Builder builder = new Options.Builder();
        builder.server(NatsConstants.NATS_PROTOCOL_SLASH_SLASH + host + ":" + port);
        builder.userInfo(username , password);
        builder.connectionName(connectionName);
        builder.connectionListener(new NatsConnectionListener());
        builder.errorListener(new NatsErrorListener());
        builder.maxReconnects(-1);
        builder.reconnectWait(Duration.ofSeconds(5));
        try {
            this.nc = Nats.connect(builder.build());
            this.js = nc.jetStream();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public NatsConfigurationTranslator(NatsConfiguration configuration , List<JetStreamBuilder> jetStreamBuilders){
        Options.Builder builder = configuration.builder();
        try {
            this.nc = Nats.connect(builder.build());
            this.js = nc.jetStream();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        boolean enableStream =  configuration.getEnableJetStream();
        if (enableStream && jetStreamBuilders != null){
            try {

                JetStreamManagement management = nc.jetStreamManagement();
                for (JetStreamBuilder streamBuilder : jetStreamBuilders) {

                    StreamConfiguration streamConfiguration = streamBuilder.builder().build();
                    String streamName = streamBuilder.getStreamName();
                    StreamInfo streamInfo = null;
                    try {
                        streamInfo = management.getStreamInfo(streamName);
                    }catch (Exception ignored){
                    }
                    if (streamInfo != null){
                        management.updateStream(streamConfiguration);
                        management.purgeStream(streamName);
                    }else {
                        management.addStream(streamConfiguration);
                    }
                }
            } catch (IOException | JetStreamApiException e) {
                e.printStackTrace();
            }

        }
    }

    public NatsConfigurationTranslator(NatsConfiguration configuration){
        this(configuration,null);
    }


    public Connection getConnection(){
        return this.nc;
    }

    public JetStream getJetStream(){
        return this.js;
    }

}
