package cn.iocoder.taro.rpc.core.transport.exception;

import java.net.InetSocketAddress;

/**
 * 通信超时异常
 *
 * 和 {@link TransportException} 基本一致，特殊点在于标记 {@link cn.iocoder.taro.rpc.core.transport.exchange.Response#STATUS_TIMEOUT}
 */
public class TransportTimeoutException extends TransportException {

    public TransportTimeoutException(InetSocketAddress localAddress, InetSocketAddress remoteAddress) {
        super(localAddress, remoteAddress);
    }

    public TransportTimeoutException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String message) {
        super(localAddress, remoteAddress, message);
    }

    public TransportTimeoutException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String message, Throwable cause) {
        super(localAddress, remoteAddress, message, cause);
    }

}