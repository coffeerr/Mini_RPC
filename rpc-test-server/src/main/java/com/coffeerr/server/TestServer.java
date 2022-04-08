package com.coffeerr.server;

import com.coffeerr.HelloService;
import com.coffeerr.registry.ServiceRegistry;
import com.coffeerr.registry.impl.ServiceRegistryImpl;
import com.coffeerr.server.impl.HelloServiceImpl;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/4/7 2:53 下午
 */

public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new ServiceRegistryImpl();
        serviceRegistry.register(helloService);

        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.start(8097);
    }
}
