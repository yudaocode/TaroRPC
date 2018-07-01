package cn.iocoder.taro.rpc.core.protocol.taro;

import cn.iocoder.taro.rpc.core.exception.TaroFrameworkException;
import cn.iocoder.taro.rpc.core.protocol.Exporter;
import cn.iocoder.taro.rpc.core.protocol.Protocol;
import cn.iocoder.taro.rpc.core.rpc.Invocation;
import cn.iocoder.taro.rpc.core.rpc.Invoker;
import cn.iocoder.taro.rpc.core.transport.Codec;
import cn.iocoder.taro.rpc.core.transport.Server;
import cn.iocoder.taro.rpc.core.transport.exchange.ExchangeHandler;
import cn.iocoder.taro.rpc.core.transport.exchange.Request;
import cn.iocoder.taro.rpc.core.transport.exchange.Response;
import cn.iocoder.taro.rpc.core.util.ClassUtil;

import java.util.HashMap;
import java.util.Map;

public class TaroProtocol implements Protocol {

    private Map<Class, Exporter> exporters = new HashMap<>();
    private Map<Integer, Server> servers = new HashMap<>();
    private static Server server;

    private ExchangeHandler handler = new ExchangeHandler() {

        @Override
        public Response reply(Request request) {
            if (!(request.getData() instanceof Invocation)) {
                throw new TaroFrameworkException("请求消息类型不对，request=" + request); // TODO 芋艿，补充下 channel ，调整为 TransportException
            }
            Invocation invocation = (Invocation) request.getData();
            Invoker invoker = lookupInvoker(invocation);
            if (invoker == null) {
                throw new TaroFrameworkException("不存在对应的服务，request=" + request); // TODO 芋艿，补充下 channel ，调整为 TransportException
            }
            // 调用结果 TODO 芋艿，此处可重构
            Response response = new Response(request.getId());
            response.setEvent(false);
            response.setStatus(Response.STATUS_SUCCESS);
            response.setData(invoker.invoke(invocation));
            return response;
        }

    };

    @Override
    public Exporter export(Invoker invoker) {
        int port = 8080;
        Server server = getServer(port);
        servers.put(port, server);

        Exporter exporter = new TaroExporter(invoker);
        exporters.put(ClassUtil.forName("cn.iocoder.taro.rpc.demo.DemoService"), exporter); // TODO 芋艿，因为没有 URL ，先暂时写死
        return exporter;
    }

    private Server getServer(int port) {
        Class serverClass = ClassUtil.forName("cn.iocoder.taro.transport.netty4.NettyServer"); // TODO 芋艿，因为没有 spi ，先暂时反射
        try {
            return (Server) serverClass.getConstructor(int.class, ExchangeHandler.class, Codec.class)
                    .newInstance(port, handler, new TaroCodec());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private Invoker lookupInvoker(Invocation invocation) {
        Exporter exporter = exporters.get(invocation.getInterfaceClass());
        if (exporter == null) {
            return null;
        }
        return exporter.getInvoker();
    }

}
