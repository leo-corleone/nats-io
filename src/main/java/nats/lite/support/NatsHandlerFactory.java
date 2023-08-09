package nats.lite.support;

import cn.hutool.extra.spring.SpringUtil;
import nats.lite.enums.ClientType;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author SunDeZhi
 * @time 2023/3/16
 * @description : 用于获取natsHandler实例的工厂类
 */
public class NatsHandlerFactory {

    private static final Map<ClientType, NatsHandler> HANDLER_MAP = new HashMap<>();


    private NatsHandlerFactory(){

    }

    /**
     * 获取对应类型的处理器
     * @param clientType 任务类型
     * @return 任务类型处理器
     */
    public static NatsHandler getHandler(ClientType clientType){

        NatsHandler handler = HANDLER_MAP.get(clientType);
        if (handler == null){
            refresh();
        }
        return HANDLER_MAP.get(clientType);
    }

    /**
     * 将所有的类型处理器刷新到内存中
     */
    private static void refresh(){

        Reflections ref = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage("nats.lite")));
        Set<Class<? extends AbstractNatsHandler>> clazzSet = ref.getSubTypesOf(AbstractNatsHandler.class);

        for (Class<? extends AbstractNatsHandler> clazz : clazzSet) {
            NatsHandler handler = SpringUtil.getBean(clazz);
            ClientType taskType = handler.getClientType();
            HANDLER_MAP.put(taskType ,  handler);
        }
    }



}
