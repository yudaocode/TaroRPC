package cn.iocoder.taro.rpc.transport;

import cn.iocoder.taro.rpc.core.transport.Request;
import cn.iocoder.taro.rpc.core.transport.Response;
import cn.iocoder.taro.rpc.transport.codec.NettyDecoder;
import cn.iocoder.taro.rpc.transport.codec.NettyEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

    private String host;
    private int port;

    private EventLoopGroup group;
    private Bootstrap bootstrap;
    private Channel channel;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;

        init();

        connect();
    }

    public void init() {
        // 配置客户端 NIO 线程组
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
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

    public void connect() {
        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();
            this.channel = future.channel();
        } catch (InterruptedException e) {
            throw new RuntimeException(e); // TODO 芋艿：异常处理
        }
    }

    public void close() {
        channel.close();
        group.shutdownGracefully();
    }

    public ResponseFuture request(Object message) {
        Request request = new Request();
        request.setData(message);
        channel.writeAndFlush(request);

        return new ResponseFuture(request.getId());
    }

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client("127.0.0.1", 8080);
        ResponseFuture future = client.request("hello");
        Object result = future.getValue();
        System.out.println(("Client:" + ((Response) result).getValue()));
    }

}
