package nats.lite.configuration;

import io.nats.client.AuthHandler;
import io.nats.client.ConnectionListener;
import io.nats.client.ErrorListener;
import io.nats.client.Options;
import io.nats.client.support.NatsConstants;

import java.time.Duration;

/**
 * @time: 2022/10/24
 * @author: .sun
 */
public class NatsConfiguration {

    /**
     * 连接端口
     *   默认: 4222
     */
    private Integer port;

    /**
     * 连接主机
     *   默认: 127.0.0.1
     */
    private String  host;

    private Boolean noEcho;

    private String connectionName;

    private Duration connectionTimeout;

    private Boolean utfSupport;

    private Integer bufferSize;

    private Duration drainTimeout;

    private Integer maxPingOut;

    private Integer maxReconnects;

    private Boolean noHeaders;

    private Boolean noReconnect;

    private Duration pingInterval;

    private Duration requestTimeout;

    private Duration reconnectWait = Options.DEFAULT_RECONNECT_WAIT;

    private Boolean enableJetStream;

    private String username = null;

    private String password = null;

    private String token = null;

    private Boolean trace;

    private ConnectionListener connectionListener;

    private ErrorListener errorListener;

    private AuthHandler authHandler;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean getNoEcho() {
        return noEcho;
    }

    public void setNoEcho(Boolean noEcho) {
        this.noEcho = noEcho;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public Duration getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Duration connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Boolean getUtfSupport() {
        return utfSupport;
    }

    public void setUtfSupport(Boolean utfSupport) {
        this.utfSupport = utfSupport;
    }

    public Integer getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    public Duration getDrainTimeout() {
        return drainTimeout;
    }

    public void setDrainTimeout(Duration drainTimeout) {
        this.drainTimeout = drainTimeout;
    }

    public Integer getMaxPingOut() {
        return maxPingOut;
    }

    public void setMaxPingOut(Integer maxPingOut) {
        this.maxPingOut = maxPingOut;
    }

    public Integer getMaxReconnects() {
        return maxReconnects;
    }

    public void setMaxReconnects(Integer maxReconnects) {
        this.maxReconnects = maxReconnects;
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

    public Duration getPingInterval() {
        return pingInterval;
    }

    public void setPingInterval(Duration pingInterval) {
        this.pingInterval = pingInterval;
    }

    public Duration getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Duration requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public Boolean getEnableJetStream() {
        return enableJetStream;
    }

    public void setEnableJetStream(Boolean enableJetStream) {
        this.enableJetStream = enableJetStream;
    }

    public ConnectionListener getConnectionListener() {
        return connectionListener;
    }

    public void setConnectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    public ErrorListener getErrorListener() {
        return errorListener;
    }

    public void setErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    public Boolean getTrace() {
        return trace;
    }

    public void setTrace(Boolean trace) {
        this.trace = trace;
    }

    public Duration getReconnectWait() {
        return reconnectWait;
    }

    public void setReconnectWait(Duration reconnectWait) {
        this.reconnectWait = reconnectWait;
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

    public AuthHandler getAuthHandler() {
        return authHandler;
    }

    public void setAuthHandler(AuthHandler authHandler) {
        this.authHandler = authHandler;
    }

    public Options.Builder builder(){

        Options.Builder builder = new Options.Builder();

        builder.server(NatsConstants.NATS_PROTOCOL_SLASH_SLASH + this.getHost() + ":" + this.getPort());
        if (this.getNoEcho()){
            builder.noEcho();
        }
        if (this.getTrace()){
            builder.traceConnection();
        }
        if (this.getNoHeaders()){
            builder.noHeaders();
        }
        if (this.getNoReconnect()){
            builder.noReconnect();
        }
        if (!(this.getToken() == null || "".equals(this.getToken()))){
            builder.token(token.toCharArray());
        }
        if (this.getAuthHandler() != null){
            builder.authHandler(this.authHandler);
        }
        if (this.getUsername() != null && this.getPassword() != null){
            builder.userInfo(this.username , this.password);
        }
        builder.maxPingsOut(this.getMaxPingOut());
        builder.maxReconnects(this.getMaxReconnects());
        builder.connectionName(this.getConnectionName());
        builder.reconnectWait(this.getReconnectWait());
        builder.pingInterval(this.getPingInterval());
        builder.bufferSize(this.getBufferSize());
        builder.connectionTimeout(this.getConnectionTimeout());
        builder.errorListener(this.getErrorListener());
        builder.connectionListener(this.getConnectionListener());


        return builder;
    }
}
