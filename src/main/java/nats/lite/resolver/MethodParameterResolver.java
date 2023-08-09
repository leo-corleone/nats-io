package nats.lite.resolver;

import io.nats.client.Message;
import io.nats.client.impl.NatsMessage;

import java.lang.reflect.Parameter;

/**
 * @author: suen
 * @time: 2023/7/28
 * @description:
 **/
public interface MethodParameterResolver {

  /**
   * 匹配对应的参数解析器
   * @param parameter 参数 反射类型
   * @return 是否匹配
   */
  boolean match(Parameter parameter);


  void assembleMessage(NatsMessage.Builder builder , Parameter parameter , Object arg);

}
