package cn.iocoder.taro.rpc.core.transport;

import java.net.InetSocketAddress;

/**
 * 端点接口
 */
public interface Endpoint {

    InetSocketAddress getLocalAddress();

    void open();

    void close();
    void close(int timeout);
    boolean isClosed();

    boolean isAvailable();

//    URL getUrl();

}