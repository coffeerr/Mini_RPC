package com.coffeerr.registry;

import com.alibaba.nacos.api.exception.NacosException;

public interface ServiceRegistry {
    <T> void register(T service);

    <T> T getService(String serviceName);
}
