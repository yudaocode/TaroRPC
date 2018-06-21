package cn.iocoder.taro.rpc.transport;

import cn.iocoder.taro.rpc.core.transport.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ServerNettyChannelManager extends ChannelInboundHandlerAdapter {

    private ConcurrentMap<String, Channel> channels = new ConcurrentHashMap<String, Channel>();

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        String channelKey = getChannelKey(ctx);
        channels.put(channelKey, new NettyChannel(ctx.channel()));
        System.out.println("添加，当前接入：" + channels.size());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        String channelKey = getChannelKey(ctx);
        channels.remove(channelKey);
        System.out.println("移除，当前接入：" + channels.size());
        super.channelUnregistered(ctx);
    }

    public Collection<Channel> getChannels() {
        return new ArrayList<Channel>(channels.values());
    }

    public Channel getChannel(InetSocketAddress address) {
        String channelKey = getChannelKey(address);
        return channels.get(channelKey);
    }

    private static String getChannelKey(ChannelHandlerContext ctx) {
        InetSocketAddress address = ((InetSocketAddress) ctx.channel().remoteAddress());
        return getChannelKey(address);
    }

    private static String getChannelKey(InetSocketAddress address) {
        return address.getHostName() + ":" + address.getPort();
    }

}