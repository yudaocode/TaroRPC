package cn.iocoder.taro.rpc.core.transport.exchange;

public interface ExchangeHandler {

    Response reply(Request request);

}