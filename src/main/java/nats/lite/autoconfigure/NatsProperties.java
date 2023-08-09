package nats.lite.autoconfigure;

import io.nats.client.Options;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author .sun
 * @date 2022/1/14
 **/

@ConfigurationProperties(prefix = NatsProperties.NATS_SERVICE)
public class NatsProperties {

    public final static String NATS_SERVICE = "spring.nats";


    /**
     * 连接端口
     *   默认: 4222
     */
    private Integer port = Options.DEFAULT_PORT;

    /**
     * 连接主机
     *   默认: 127.0.0.1
     */
    private String  host = "127.0.0.1";

    /**
     * 重连等待时间
     *   默认: 2000ms
     */
    private Duration reconnectWait = Options.DEFAULT_RECONNECT_WAIT;

    /**
     * 连接超时
     *   默认: 2s
     */
    private Duration connectionTimeout = Options.DEFAULT_CONNECTION_TIMEOUT;

    /**
     * 最大重连次数
     *   默认: -1 一直尝试重连
     */
    private Integer maxReconnects = -1;

    /**
     * ping间隔周期
     *   默认: 2m
     */
    private long pingInterval = Options.DEFAULT_PING_INTERVAL.getSeconds();

    private int maxPingsOut = 2;

    private String connectionName = Options.PROP_CONNECTION_NAME;

    private String inboxPrefix = Options.DEFAULT_INBOX_PREFIX;

    private Integer bufferSize = Options.DEFAULT_BUFFER_SIZE;

    private Boolean trace = Boolean.FALSE;

    private Boolean enableJetStream = Boolean.FALSE;

    private String username = null;

    private String password = null;

    private String token = null;

    private Boolean utf8Support = Boolean.TRUE;

    private Boolean noEcho = Boolean.FALSE;

    private Boolean noHeaders =  Boolean.FALSE;

    private Boolean noReconnect = Boolean.FALSE;

    private Duration requestTimeout = Duration.ofSeconds(10);

    private Duration drainTimeout = Duration.ofSeconds(20);


    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Duration getReconnectWait() {
        return reconnectWait;
    }

    public void setReconnectWait(Duration reconnectWait) {
        this.reconnectWait = reconnectWait;
    }

    public Duration getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Duration connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getMaxReconnects() {
        return maxReconnects;
    }

    public void setMaxReconnects(Integer maxReconnects) {
        this.maxReconnects = maxReconnects;
    }

    public long getPingInterval() {
        return pingInterval;
    }

    public void setPingInterval(long pingInterval) {
        this.pingInterval = pingInterval;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getInboxPrefix() {
        return inboxPrefix;
    }

    public void setInboxPrefix(String inboxPrefix) {
        this.inboxPrefix = inboxPrefix;
    }

    public Integer getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    public Boolean getTrace() {
        return trace;
    }

    public void setTrace(Boolean trace) {
        this.trace = trace;
    }

    public Boolean getEnableJetStream() {
        return enableJetStream;
    }

    public void setEnableJetStream(Boolean enableJetStream) {
        this.enableJetStream = enableJetStream;
    }

    public int getMaxPingsOut() {
        return maxPingsOut;
    }

    public void setMaxPingsOut(int maxPingsOut) {
        this.maxPingsOut = maxPingsOut;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getUtf8Support() {
        return utf8Support;
    }

    public void setUtf8Support(Boolean utf8Support) {
        this.utf8Support = utf8Support;
    }

    public Boolean getNoEcho() {
        return noEcho;
    }

    public void setNoEcho(Boolean noEcho) {
        this.noEcho = noEcho;
    }

    public Boolean getNoHeaders() {
        return noHeaders;
    }

    public void setNoHeaders(Boolean noHeaders) {
        this.noHeaders = noHeaders;
    }

    public Boolean getNoReconnect() {
        return noReconnect;
    }

    public void setNoReconnect(Boolean noReconnect) {
        this.noReconnect = noReconnect;
    }

    public Duration getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Duration requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public Duration getDrainTimeout() {
        return drainTimeout;
    }

    public void setDrainTimeout(Duration drainTimeout) {
        this.drainTimeout = drainTimeout;
    }
}
