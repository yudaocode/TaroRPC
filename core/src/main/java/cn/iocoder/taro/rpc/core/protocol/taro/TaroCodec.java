package cn.iocoder.taro.rpc.core.protocol.taro;

import cn.iocoder.taro.rpc.core.exception.TaroFrameworkException;
import cn.iocoder.taro.rpc.core.rpc.support.RpcInvocation;
import cn.iocoder.taro.rpc.core.rpc.support.RpcResult;
import cn.iocoder.taro.rpc.core.serialize.Serialization;
import cn.iocoder.taro.rpc.core.serialize.json.FastJSONSerialization;
import cn.iocoder.taro.rpc.core.transport.Channel;
import cn.iocoder.taro.rpc.core.transport.exchange.Request;
import cn.iocoder.taro.rpc.core.transport.exchange.Response;
import cn.iocoder.taro.rpc.core.transport.support.AbstractCodec;
import cn.iocoder.taro.rpc.core.util.ClassUtil;
import cn.iocoder.taro.rpc.core.util.ExceptionUtil;
import cn.iocoder.taro.rpc.core.util.IOUtil;

import java.io.*;

public class TaroCodec extends AbstractCodec {

    /**
     * 响应 - 异常
     */
    private static final byte RESPONSE_WITH_EXCEPTION = 0;
    /**
     * 响应 - 正常（空返回）
     */
    private static final byte RESPONSE_VALUE = 1;
    /**
     * 响应 - 正常（有返回）
     */
    private static final byte RESPONSE_NULL_VALUE = 2;

    // TODO Serialization 没弄好
    private final Serialization serialization = new FastJSONSerialization();

    @Override
    public byte[] encodeBody(Channel channel, Object message) {
        // Request
        if (message instanceof Request) {
            return encodeRequestBody(channel, (Request) message);
        }
        // Response
        if (message instanceof Response) {
            return encodeResponseBody(channel, (Response) message);
        }
        throw new TaroFrameworkException("编码失败：exception=未知的数据类型");
    }

    @Override
    public Object decodeBody(Channel channel, Object message, byte[] bodyBytes) {
        // Request
        if (message instanceof Request) {
            return decodeRequestBody(channel, (Request) message, bodyBytes);
        }
        // Response
        if (message instanceof Response) {
            return decodeResponseBody(channel, (Response) message, bodyBytes);
        }
        throw new TaroFrameworkException("解码失败：exception=未知的数据类型");
    }

    private byte[] encodeRequestBody(Channel channel, Request request) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput output = super.createObjectOutput(outputStream);

        try {
            RpcInvocation invocation = (RpcInvocation) request.getData();
            output.writeUTF(invocation.getInterfaceClass().getName());
            output.writeUTF(invocation.getMethodName());
//            output.writeUTF(invocation.getArgumentTypes()); // TODO 芋艿，方法签名

            // 方法参数（无需写入长度，因为上面 argumentTypes 已经足够获得参数个数）
            for (Object argument : invocation.getArguments()) {
                output.writeObject(serialization.serialize(argument));
            }

            output.flush();
            return outputStream.toByteArray();
        } catch (Throwable e) {
            throw new TaroFrameworkException("编码 Request 失败：exception=" + ExceptionUtil.getMessage(e));
        } finally {
            IOUtil.close(output, outputStream);
        }
    }

    private byte[] encodeResponseBody(Channel channel, Response response) {
        ByteArrayOutputStream outputStream = null;
        ObjectOutput output = null;
        try {
            if (response.getStatus() == Response.STATUS_SUCCESS) {
                outputStream = new ByteArrayOutputStream();
                output = super.createObjectOutput(outputStream);

                RpcResult result = (RpcResult) response.getData();
                if (result.getException() != null) {
                    output.writeByte(RESPONSE_WITH_EXCEPTION);
                    output.writeUTF(result.getException().getClass().getName());
                    output.writeObject(serialization.serialize(result.getException()));
                } else if (result.getValue() != null) {
                    output.writeByte(RESPONSE_VALUE);
                    output.writeUTF(result.getValue().getClass().getName());
                    output.writeObject(serialization.serialize(result.getValue()));
                } else {
                    output.write(RESPONSE_NULL_VALUE);
                }
                return outputStream.toByteArray();
            } else {
                return serialization.serialize(response.getErrorMsg());
            }
        } catch (Throwable e) {
            throw new TaroFrameworkException("编码 Response 失败：exception=" + ExceptionUtil.getMessage(e));
        } finally {
            IOUtil.close(output, outputStream);
        }
    }

    private Object decodeRequestBody(Channel channel, Request request, byte[] bodyBytes) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bodyBytes);
        ObjectInput input = super.createObjectInput(inputStream);

        RpcInvocation invocation = new RpcInvocation();
        try {
            invocation.setInterfaceClass(ClassUtil.forName(input.readUTF()));
            invocation.setMethodName(input.readUTF());
            // argumentTypes TODO
            Class<?>[] argumentTypes = new Class<?>[]{String.class};

            // 方法参数
            Object[] arguments = new Object[argumentTypes.length];
            for (int i = 0; i < argumentTypes.length; i++) {
                arguments[i] =  serialization.deserialize((byte[]) input.readObject(), argumentTypes[i]);
            }
            invocation.setArguments(arguments);
        } catch (Throwable e) {
            throw new TaroFrameworkException("解码 Request 失败：exception=" + ExceptionUtil.getMessage(e));
        } finally {
            IOUtil.close(input, inputStream);
        }

        return serialization.deserialize(bodyBytes, null);
    }

    private Object decodeResponseBody(Channel channel, Response response, byte[] bodyBytes) {
        ByteArrayInputStream inputStream = null;
        ObjectInput input = null;

        try {
            if (response.getStatus() == Response.STATUS_SUCCESS) {
                inputStream = new ByteArrayInputStream(bodyBytes);
                input = super.createObjectInput(inputStream);

                byte flag = input.readByte();
                if (flag == RESPONSE_WITH_EXCEPTION) {
                    Class exceptionClass = ClassUtil.forName(input.readUTF());
                    return new RpcResult(serialization.deserialize((byte[]) input.readObject(), exceptionClass));
                } else if (flag == RESPONSE_VALUE) {
                    Class valueClass = ClassUtil.forName(input.readUTF());
                    return new RpcResult(serialization.deserialize((byte[]) input.readObject(), valueClass));
                } else {
                    return null;
                }
            } else {
                return serialization.deserialize(bodyBytes, String.class);
            }
        } catch (Throwable e) {
            throw new TaroFrameworkException("解码 Request 失败：exception=" + ExceptionUtil.getMessage(e));
        } finally {
            IOUtil.close(input, inputStream);
        }
    }

}