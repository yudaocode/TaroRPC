package cn.iocoder.taro.rpc.core.transport.exchange;

import cn.iocoder.taro.rpc.core.transport.Channel;
import cn.iocoder.taro.rpc.core.transport.MessageHandler;
import cn.iocoder.taro.rpc.core.transport.exception.TransportException;
import cn.iocoder.taro.rpc.core.util.ExceptionUtil;

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
            // 回复请求
            Request request = (Request) message;
            Response response;
            try {
                response = exchangeHandler.reply(request);
            } catch (Throwable th) {
                // TODO 芋艿，打印错误日志
                response = new Response(request.getId()).setEvent(false).setStatus(Response.STATUS_SERVICE_ERROR)
                        .setErrorMsg(ExceptionUtil.getMessage(th));
            }
            if (response != null) {
                try {
                    channel.send(response);
                } catch (TransportException e) {
                    e.printStackTrace(); // TODO 芋艿，打印错误日志
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