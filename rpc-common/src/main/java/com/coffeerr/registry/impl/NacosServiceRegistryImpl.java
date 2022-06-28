package com.coffeerr.registry.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.coffeerr.registry.ServiceRegistry;
import com.coffeerr.utils.NacosUtil;
import lombok.AllArgsConstructor;

import java.net.InetSocketAddress;

/**
 * @description: 引入Nacos之后，需要将原先的Registry类进行耦合，将本地Map的配置耦合写入Provider中
 * @author: Desmond
 * @time: 2022/6/27 5:54 PM
 */

@AllArgsConstructor
public class NacosServiceRegistryImpl implements ServiceRegistry {
    private InetSocketAddress inetSocketAddress;


    @Override
    public <T> void register(T service) {
        try {
            NacosUtil.registerService(service.getClass().getCanonicalName(), inetSocketAddress);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T getService(String serviceName) {
        return null;
    }
}
