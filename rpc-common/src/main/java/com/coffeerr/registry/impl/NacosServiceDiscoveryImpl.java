package com.coffeerr.registry.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.coffeerr.registry.ServiceDiscovery;
import com.coffeerr.utils.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/6/28 9:21 AM
 */

public class NacosServiceDiscoveryImpl implements ServiceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceDiscoveryImpl.class);

    @Override
    public InetSocketAddress serviceDiscover(String serviceName) {
        try {
            //利用列表获取某个服务的所有提供者
            List<Instance> instances = NacosUtil.getAllInstance(serviceName);
            Instance instance = instances.get(0);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            logger.error("获取服务时有错误发生", e);
        }
        return null;

    }
}
