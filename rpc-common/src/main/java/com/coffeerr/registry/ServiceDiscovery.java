package com.coffeerr.registry;

import com.alibaba.nacos.api.exception.NacosException;

import java.net.InetSocketAddress;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/6/28 9:20 AM
 */

public interface ServiceDiscovery {
    InetSocketAddress serviceDiscover(String serviceName) throws NacosException;
}
