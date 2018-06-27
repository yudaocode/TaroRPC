package cn.iocoder.taro.rpc.core.transport.exception;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 通信异常
 *
 * 参考自 Dubbo RemotingException 设计
 */
public class TransportException extends IOException {

    private final InetSocketAddress localAddress;
    private final InetSocketAddress remoteAddress;

    public TransportException(InetSocketAddress localAddress, InetSocketAddress remoteAddress) {
        this(localAddress, remoteAddress, null, null);
    }

    public TransportException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String message) {
        this(localAddress, remoteAddress, message, null);
    }

    public TransportException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String message, Throwable cause) {
        super(message, cause);
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

}