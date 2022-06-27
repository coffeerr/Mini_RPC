package com.coffeerr.registry.impl;

import com.coffeerr.registry.ServiceRegistry;
import com.coffeerr.utils.NacosUtil;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/6/27 5:54 PM
 */

public class NacosServiceRegistryImpl implements ServiceRegistry {
    @Override
    public <T> void register(T service) {
    }

    @Override
    public <T> T getService(String serviceName) {
        return null;
    }
}
