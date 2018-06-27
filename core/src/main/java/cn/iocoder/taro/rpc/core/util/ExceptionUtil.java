package cn.iocoder.taro.rpc.core.util;

import org.apache.commons.lang.exception.ExceptionUtils;

public class ExceptionUtil {

    public static String getMessage(Throwable th) {
        return ExceptionUtils.getMessage(th);
    }

}