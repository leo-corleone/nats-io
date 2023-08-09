package nats.lite.resolver;

import com.alibaba.fastjson.JSONObject;
import nats.lite.enums.ClientType;
import io.nats.client.Message;

/**
 * @author SunDeZhi
 * @time 2023/8/1
 * @description :
 */
public class DefaultReturnValueResolver implements  ReturnValueResolver{
    @Override
    public boolean match(ClientType clientType) {
        return true;
    }

    @Override
    public Object convertResult(Message message, Class<?> returnType) {
        byte[] data = message.getData();
        if (data == null){
            return null;
        }
        return JSONObject.parseObject(data , returnType);
    }
}
