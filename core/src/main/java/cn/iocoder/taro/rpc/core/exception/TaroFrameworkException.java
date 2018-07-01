package cn.iocoder.taro.rpc.core.exception;

public class TaroFrameworkException extends AbstractTaroException {

    public static final int CODE_UNKNOWN_EXCEPTION = 0;

    private int code = CODE_UNKNOWN_EXCEPTION;

    public TaroFrameworkException(String message) {
        super(message);
    }

    public TaroFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

}