package com.coffeerr.provider;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/6/28 10:24 AM
 */

public interface ServiceProvider {

    /**
     * 将service添加进本地的ServiceMap
     *
     * @param service
     * @param serviceClass
     * @param <T>
     */
    <T> void addServiceProvider(T service, Class<T> serviceClass);

    /**
     * 从本地的ServiceMap获得Service
     *
     * @param serviceName
     * @return
     */
    Object getServiceProvider(String serviceName);
}
