package cn.iocoder.taro.rpc.core.transport.support;

import cn.iocoder.taro.rpc.core.exception.TaroFrameworkException;
import cn.iocoder.taro.rpc.core.transport.Codec;
import cn.iocoder.taro.rpc.core.util.ExceptionUtil;

import java.io.*;

public abstract class AbstractCodec implements Codec {

    protected ObjectOutput createObjectOutput(OutputStream output) {
        try {
            return new ObjectOutputStream(output);
        } catch (IOException e) {
            throw new TaroFrameworkException("创建 ObjectOutput 失败：exception=" + ExceptionUtil.getMessage(e));
        }
    }

    protected ObjectInput createObjectInput(InputStream input) {
        try {
            return new ObjectInputStream(input);
        } catch (IOException e) {
            throw new TaroFrameworkException("创建 ObjectInput 失败：exception=" + ExceptionUtil.getMessage(e));
        }
    }

}