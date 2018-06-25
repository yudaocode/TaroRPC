package cn.iocoder.taro.rpc.core.transport;

public interface Client extends Channel {

    Channel getChannel();

    void reconnect();

}