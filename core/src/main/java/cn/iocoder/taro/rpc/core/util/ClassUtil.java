package cn.iocoder.taro.rpc.core.util;

public class ClassUtil {

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className); // TODO 芋艿，进一步优化
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

}