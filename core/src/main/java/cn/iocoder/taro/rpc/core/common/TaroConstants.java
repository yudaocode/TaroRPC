package cn.iocoder.taro.rpc.core.common;

public class TaroConstants {

    // ========== 通信模块相关 ==========
    /**
     * 通信模块 - 客户端空闲模块，单位：毫秒
     */
    public static int TRANSPORT_CLIENT_IDLE = 150 * 1000; // TODO 芋艿，这里先硬编码
    /**
     * 通信模块 - 服务端空闲时间，单位：毫秒
     */
    public static int TRANSPORT_SERVER_IDLE = 900 * 1000; // TODO 芋艿，这里先硬编码

}