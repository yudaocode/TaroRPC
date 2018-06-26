package cn.iocoder.taro.rpc.core.transport.exchange;

import cn.iocoder.taro.rpc.core.transport.Channel;
import cn.iocoder.taro.rpc.core.transport.MessageHandler;
import cn.iocoder.taro.rpc.core.transport.TransportException;

import java.util.concurrent.ThreadPoolExecutor;

public class ExchangeMessageHandler implements MessageHandler {

    private ThreadPoolExecutor executor;

    private ExchangeHandler exchangeHandler;

    public ExchangeMessageHandler(ThreadPoolExecutor executor, ExchangeHandler exchangeHandler) {
        this.executor = executor;
        this.exchangeHandler = exchangeHandler;
    }

    @Override
    public void handle(final Channel channel, final Object message) {
        if (executor != null) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    processMessage(channel, message);
                }
            });
            // TODO 芋艿：失败情况下的处理方式。
        } else {
            processMessage(channel, message);
        }
    }

    private void processMessage(Channel channel, Object message) {
        if (message instanceof Request) {
            if (exchangeHandler == null) { // 对于客户端，目前暂时不需要处理。 TODO 芋艿，后面要添加下对 read_only 事件的处理
                return;
            }
            Response response = exchangeHandler.reply((Request) message);
            if (response != null) {
                try {
                    channel.send(response);
                } catch (TransportException e) {
                    e.printStackTrace(); // TODO 芋艿，后续的处理。
                }
            }
        } else if (message instanceof Response) {
            Response response = (Response) message;
            InvokeFuture future = InvokeFuture.getFuture(response.getId());
            if (future == null) {
                return; // TODO 芋艿，后续完善
            }
            future.notifyResponse(response);
        }
    }

}