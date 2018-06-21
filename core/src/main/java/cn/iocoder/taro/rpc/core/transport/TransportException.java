package cn.iocoder.taro.rpc.core.transport;

import java.io.IOException;
import java.net.InetSocketAddress;

public class TransportException extends IOException {

    private final InetSocketAddress localAddress;
    private final InetSocketAddress remoteAddress;

    public TransportException(InetSocketAddress localAddress, InetSocketAddress remoteAdddress) {
        this.localAddress = localAddress;
        this.remoteAddress = remoteAdddress;
    }

    public TransportException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String message) {
        super(message);
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