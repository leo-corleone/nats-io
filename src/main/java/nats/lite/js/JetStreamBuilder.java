package nats.lite.js;

import io.nats.client.api.DiscardPolicy;
import io.nats.client.api.RetentionPolicy;
import io.nats.client.api.StorageType;
import io.nats.client.api.StreamConfiguration;

import java.time.Duration;

/**
 * @time: 2022/10/24
 * @author: .sun
 */

public class JetStreamBuilder {

    private final StreamConfiguration.Builder builder ;

    private String streamName;

    public JetStreamBuilder(){
        builder = StreamConfiguration.builder();
    }


    public void setStreamName(String streamName){
        builder.name(streamName);
        this.streamName = streamName;
    }

    public void setSubjects(String ...subjects){
        builder.subjects(subjects);
    }

    public void setStorageType(StorageType storageType){
        builder.storageType(storageType);
    }

    public void setDuplicateWindow(Duration duplicateWindow){
        builder.duplicateWindow(duplicateWindow);
    }

    public void setDuplicateWindow(long duplicateWindow){
        builder.duplicateWindow(duplicateWindow);
    }

    public void setMaxConsumers(long maxConsumers){
        builder.maxConsumers(maxConsumers);
    }

    public void setMaxMessages(long maxMessages){
        builder.maxMessages(maxMessages);
    }

    public void setReplicas(int replicas){
        builder.replicas(replicas);
    }

    public void setRetentionPolicy(RetentionPolicy retentionPolicy){
        builder.retentionPolicy(retentionPolicy);
    }

    public void setDiscardPolicy(DiscardPolicy discardPolicy){
        builder.discardPolicy(discardPolicy);
    }

    public void setMaxAge(Duration maxAge){
        builder.maxAge(maxAge);
    }

    public void setMaxAge(long maxAge){
        builder.maxAge(maxAge);
    }

    public void setMaxBytes(long maxBytes){
        builder.maxBytes(maxBytes);
    }

    public void setMaxMessagesPerSubject(long maxMessagesPerSubject){
        builder.maxMessagesPerSubject(maxMessagesPerSubject);
    }

    public void setMaxMsgSize(long maxMsgSize){
        builder.maxMsgSize(maxMsgSize);
    }

    public void setDescription(String description){
        builder.description(description);
    }

    public StreamConfiguration.Builder builder() {
        return builder;
    }

    public String getStreamName() {
        return streamName;
    }
}
