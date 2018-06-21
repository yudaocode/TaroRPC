package cn.iocoder.taro.rpc.core.transport;

public interface ResponseCallback {

    void onSuccess(Object value);

    void onFailure(Throwable throwable);

}