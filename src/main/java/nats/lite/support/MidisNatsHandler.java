package nats.lite.support;

import nats.lite.configuration.NatsConfigurationTranslator;
import nats.lite.enums.ClientType;
import org.springframework.stereotype.Component;

/**
 * @author SunDeZhi
 * @time 2023/2/15
 * @description :
 */

@Component
public class MidisNatsHandler extends AbstractNatsHandler {



    private final String username = "AppPlat";


    /**
     * 42cc78285138543213947feac70eaf1b
     * MD加密 Tendzone123
     */
    private final String password = "__42cc78285138543213947feac70eaf1b";


    private final String connectionName = "TZ_APP_Midis";



    public MidisNatsHandler() {
        this.clientType = ClientType.Midis;
    }


    /**
     * 支持的平台客户端
     * @param subject 使用主题匹配
     */
    @Override
    public boolean match(String subject) {
        return subject.contains("mvcm") || subject.contains("mwin") || subject.contains("devinf")
            || subject.contains("msrc") || subject.contains("ctrlexe") || subject.contains("ctrlinf")
            || subject.contains("apmng");
    }



//    @PostConstruct
    public void init(){
        init("10.9.8.24" , 18300);
    }


    /**
     * 用于nats初始化连接
     *
     * @param host 主机
     * @param port 端口号
     */
    @Override
    public void init(String host, Integer port) {
        destroy();
        init(new NatsConfigurationTranslator(host , port , username ,password , connectionName));
    }

    @Override
    public void init(String host, Integer port , String username , String password) {
        // 断开之前的连接
        destroy();
        init(new NatsConfigurationTranslator(host , port , username ,password , connectionName));
    }
}
