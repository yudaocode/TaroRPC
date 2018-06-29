package cn.iocoder.taro.rpc.core.util;

public class IOUtil {

    public static void close(AutoCloseable... closeables) {
        for (AutoCloseable closeable : closeables) {
            try {
                closeable.close();
            } catch (Exception e) {
                // nothing
            }
        }
    }

}
