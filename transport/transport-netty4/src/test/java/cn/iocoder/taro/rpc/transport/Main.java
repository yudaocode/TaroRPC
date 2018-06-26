package cn.iocoder.taro.rpc.transport;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Main {

    public static void main(String[] args) {
        ConcurrentMap<String, String> map = new ConcurrentHashMap<String, String>();
        map.put("1", "2");
        map.put("2", "3");
        map.put("3", "4");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            map.remove(entry.getKey());
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
    }

}