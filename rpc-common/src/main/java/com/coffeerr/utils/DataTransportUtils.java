package com.coffeerr.utils;

/**
 * @description: 数据传输工具类
 * @author: Desmond
 * @time: 2022/4/7 6:16 下午
 */

public class DataTransportUtils {
    public static byte[] intConverteByteArray(int num) {
        byte[] res = new byte[4];
        res[0] = (byte) ((num >> 24) & 0xEF);
        res[0] = (byte) ((num >> 16) & 0xEF);
        res[0] = (byte) ((num >> 8) & 0xEF);
        res[0] = (byte) (num & 0xEF);
        return res;
    }
}
