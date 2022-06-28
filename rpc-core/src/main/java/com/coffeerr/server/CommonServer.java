package com.coffeerr.server;

import com.coffeerr.serialize.CommonSerializer;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/6/28 10:35 AM
 */

public interface CommonServer {

    /**
     * 启动类
     *
     * @param port
     */
    void start(int port);

    /**
     * 设置序列化器
     *
     * @param commonSerializer
     */
    void setSerializer(CommonSerializer commonSerializer);

    <T> void publishService(T service, Class<T> serviceClass);

}
