package cn.iocoder.taro.rpc.core.transport;

import cn.iocoder.taro.rpc.core.transport.exchange.Response;
import cn.iocoder.taro.rpc.core.transport.exchange.ResponseCallback;
import cn.iocoder.taro.rpc.core.transport.exchange.ResponseFuture;

import java.net.InetSocketAddress;

/**
 * 通道接口
 *
 * 考虑到简化整体类关系，在实现上类似 Dubbo 的 Channel + ExchangeChannel 两个接口的合集，即增加了：
 *
 * 1. oneway
 * 2. requestSync
 * 3. requestAsync
 * 4. requestWithCallback
 */
public interface Channel extends Endpoint {

    InetSocketAddress getRemoteAddress();

    void send(Object message) throws TransportException;

    void oneway(Object request) throws TransportException;

    Response requestSync(Object request) throws InterruptedException, TransportException;

    ResponseFuture requestAsync(Object request) throws TransportException;

    void requestWithCallback(Object request, ResponseCallback callback) throws TransportException;

}