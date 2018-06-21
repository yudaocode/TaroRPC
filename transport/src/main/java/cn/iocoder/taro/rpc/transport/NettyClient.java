package cn.iocoder.taro.rpc.transport;

import cn.iocoder.taro.rpc.core.transport.Channel;
import cn.iocoder.taro.rpc.core.transport.Response;
import cn.iocoder.taro.rpc.core.transport.ResponseFuture;
import cn.iocoder.taro.rpc.core.transport.TransportException;
import cn.iocoder.taro.rpc.core.transport.support.AbstractClient;
import cn.iocoder.taro.rpc.transport.codec.NettyDecoder;
import cn.iocoder.taro.rpc.transport.codec.NettyEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;

public class NettyClient extends AbstractClient {

    private EventLoopGroup group;
    private Bootstrap bootstrap;
    private NettyChannel channel;

    public NettyClient(String host, int port) {
        super(host, port);
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
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast("decoder", new NettyDecoder())
                                .addLast("encoder", new NettyEncoder())
                                .addLast(new ClientHandler());
                    }
                });
    }

    private void connect() {
        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();
            this.channel = new NettyChannel(future.channel());
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

    public static void main(String[] args) throws InterruptedException, TransportException {
        NettyClient client = new NettyClient("127.0.0.1", 8080);
//        NettyClient client = new NettyClient("192.168.16.23", 8080);
        ResponseFuture future = client.requestAsync("hello");
        Object result = future.getValue();
        System.out.println(("Client:" + ((Response) result).getValue()));

        client.send("nihao");

//        client.close();

        System.out.println("pid:" + ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);

        for (int i = 0; i < 100; i++) {
            try {
                Object result2 = future.getValue();
                System.out.println(("Client:" + ((Response) result2).getValue()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(10 * 1000L);
        }

//        System.exit(-1);
    }


}
