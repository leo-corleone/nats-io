package nats.lite.proxy;

/**
 * @author SunDeZhi
 * @time 2023/7/28
 * @description :
 */
public interface Targeter {


    /**
     * 获取反射对应的实例化类
     * @param clazz 反射类
     * @return 实例化
     * @param <T> 泛型
     */
    <T> T target(Class<?> clazz);

}
