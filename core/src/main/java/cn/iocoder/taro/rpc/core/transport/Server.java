package cn.iocoder.taro.rpc.core.transport;

import java.net.InetSocketAddress;
import java.util.Collection;

public interface Server extends Endpoint {

    boolean isBond();

    Collection<Channel> getChannels();

    Channel getChannel(InetSocketAddress address);

}