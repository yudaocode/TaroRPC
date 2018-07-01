package cn.iocoder.taro.transport.netty4;

import cn.iocoder.taro.rpc.core.common.TaroConstants;
import cn.iocoder.taro.rpc.core.transport.Channel;
import cn.iocoder.taro.rpc.core.transport.Codec;
import cn.iocoder.taro.rpc.core.transport.exchange.ExchangeHandler;
import cn.iocoder.taro.rpc.core.transport.support.AbstractClient;
import cn.iocoder.taro.transport.netty4.codec.NettyDecoder;
import cn.iocoder.taro.transport.netty4.codec.NettyEncoder;
import cn.iocoder.taro.transport.netty4.heartbeat.ClientHeartbeatHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class NettyClient extends AbstractClient {

    private EventLoopGroup group;
    private Bootstrap bootstrap;
    private NettyChannel channel;

    public NettyClient(String host, int port, ExchangeHandler exchangeHandler) {
        super(host, port, exchangeHandler);
    }

    // TODO 芋艿
    public NettyClient(String host, int port, ExchangeHandler exchangeHandler, Codec codec) {
        super(host, port, exchangeHandler, codec);
    }

    @Override
    public void open() {
        this.init();
        this.connect();
    }

    private void init() {
        // 配置客户端 NIO 线程组
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast("decoder", new NettyDecoder(codec))
                                .addLast("encoder", new NettyEncoder(codec))
                                .addLast("idleState", new IdleStateHandler(TaroConstants.TRANSPORT_CLIENT_IDLE, TaroConstants.TRANSPORT_CLIENT_IDLE, 0, TimeUnit.MILLISECONDS))
                                .addLast("heartbeat", new ClientHeartbeatHandler())
                                .addLast("handler", new NettyChannelHandler(messageHandler));
                    }
                });
    }

    private void connect() {
        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();
            this.channel = new NettyChannel(future.channel());
//            future.channel().attr(NettyChannel.ATTR_CHANNEL).set(this.channel); // 后续移除，已经添加到 NettyChannel
        } catch (InterruptedException e) {
            throw new RuntimeException(e); // TODO 芋艿：异常处理
        }
    }

    @Override
    public void close() {
        super.close();

        channel.close();
        group.shutdownGracefully();
    }

    @Override
    public void close(int timeout) {
        // TODO 优雅关闭
    }

    @Override
    public void reconnect() { // TODO 芋艿，父类的
        // TODO 重构成 disconnect
        channel.close();
        // 连接
        connect();
    }

    @Override
    public boolean isAvailable() {
//        return channel != null && channel.isActive();
        return false; // TODO 芋艿，后续实现
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
//        return channel != null ? (InetSocketAddress) channel.localAddress() : null;
        return null;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
//        return channel != null ? (InetSocketAddress) channel.remoteAddress() : null;
        return null;
    }

//    public static void main(String[] args) throws InterruptedException, TransportException {
//        NettyClient client = new NettyClient("127.0.0.1", 8080, null);
////        NettyClient client = new NettyClient("192.168.16.23", 8080);
//        ResponseFuture future = client.requestAsync("hello");
//        Object result = future.waitResponse();
//        System.out.println(("Client:" + ((Response) result).waitResponse()));
//
//        client.requestSync("nihao");
//
////        client.close();
//
////        System.out.println("pid:" + ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
////
////        for (int i = 0; i < 100; i++) {
////            try {
////                Object result2 = future.waitResponse();
////                System.out.println(("Client:" + ((Response) result2).waitResponse()));
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////            Thread.sleep(10 * 1000L);
////        }
//
////        System.exit(-1);
//    }


}
