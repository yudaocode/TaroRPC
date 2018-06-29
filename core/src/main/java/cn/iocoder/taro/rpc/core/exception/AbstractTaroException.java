package cn.iocoder.taro.rpc.core.exception;

public abstract class AbstractTaroException extends RuntimeException {

    public AbstractTaroException(String message) {
        super(message);
    }

    public AbstractTaroException(String message, Throwable cause) {
        super(message, cause);
    }
}
