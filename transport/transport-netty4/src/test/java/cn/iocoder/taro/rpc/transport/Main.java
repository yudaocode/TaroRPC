package cn.iocoder.taro.rpc.transport;

import com.alibaba.fastjson.JSON;

public class Main {

    public static void main(String[] args) {
//        ConcurrentMap<String, String> map = new ConcurrentHashMap<String, String>();
//        map.put("1", "2");
//        map.put("2", "3");
//        map.put("3", "4");
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            map.remove(entry.getKey());
//            System.out.println(entry.getKey() + "\t" + entry.getData());
//        }
//        ParserConfig.getGlobalInstance().addAccept("java.lang.");
//        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
//        try {
//            int k = 1 / 0;
//        } catch (Exception e) {
//            String str = JSON.toJSONString(e, SerializerFeature.WriteEnumUsingToString, SerializerFeature.WriteClassName);
//            System.out.println(JSON.parse(str));
//        }
        String hello = "hello";
        byte[] bytes = JSON.toJSONBytes(hello);
        System.out.println(JSON.parse(bytes));
    }

}