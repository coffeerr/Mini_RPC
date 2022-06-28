package com.coffeerr.provider;

import com.coffeerr.constants.RpcError;
import com.coffeerr.exception.RpcException;
import com.coffeerr.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @description:
 * @author: Desmond
 * @time: 2022/6/28 10:27 AM
 */

public class ServiceProviderImpl implements ServiceProvider {
    public static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);

    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    /**
     * 用来存放实现类的名称，Set存取更高效，存放实现类名称相比存放接口名称占的空间更小，因为一个实现类可能实现了多个接口，查找效率也会更高
     */
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public <T> void addServiceProvider(T service, Class<T> serviceClass) {
        String serviceImplName = service.getClass().getCanonicalName();
        if (registeredService.contains(serviceImplName)) {
            return;
        }
        registeredService.add(serviceImplName);
        //可能实现了多个接口，故使用Class数组接收
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for (Class<?> i : interfaces) {
            serviceMap.put(i.getCanonicalName(), service);
        }
        logger.info("向接口：{} 注册服务：{}", interfaces, serviceImplName);
    }

    @Override
    public Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_REGISTERED);
        }
        return service;
    }
}
