package nats.lite.support;

import nats.lite.configuration.NatsConfigurationTranslator;
import nats.lite.enums.ClientType;
import org.springframework.stereotype.Component;

/**
 * @author SunDeZhi
 * @time 2023/5/18
 * @description : A线nats客户端
 */

public class AudioLineNatsLiteHandler extends AbstractNatsLiteHandler {


    /**
     * 用户名
     */
    private final String username = "CloudMIDIS";


    /**
     * 密码
     */
    private final String password = "__abc123abc123abc123abc123abc123ab";



    public AudioLineNatsLiteHandler() {
        this.clientType = ClientType.Audio_Line;
    }


    /**
     * 用于nats初始化连接
     *
     * @param host 主机
     * @param port 端口号
     */
    @Override
    public void init(String host, Integer port) {
        init(host, port , username , password);
    }

    /**
     * 用于nats初始化连接
     *
     * @param host     主机
     * @param port     端口号
     * @param username 用户名
     * @param password 密码
     */
    @Override
    public void init(String host, Integer port, String username, String password) {
        super.init(new NatsConfigurationTranslator(host , port , username , password , "TZ_Audio_Line"));
    }

    /**
     * 支持的平台客户端
     *
     * @param subject 使用主题匹配
     */
    @Override
    public boolean match(String subject) {
        return subject.contains("audioprocr");
    }
}
