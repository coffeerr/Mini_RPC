package com.coffeerr.server;

import com.coffeerr.HelloService;
import com.coffeerr.MorningService;
import com.coffeerr.registry.ServiceRegistry;
import com.coffeerr.registry.impl.ServiceRegistryImpl;
import com.coffeerr.server.impl.HelloServiceImpl;
import com.coffeerr.server.impl.MorningServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/4/7 2:53 下午
 */

public class TestServer {
    public static void main(String[] args) {
    }

    @Test
    @DisplayName("多服务注册-服务端")
    public void testMultiServiceServer() {
        HelloService helloService = new HelloServiceImpl();
        MorningService morningService = new MorningServiceImpl();
        ServiceRegistry serviceRegistry = new ServiceRegistryImpl();
        serviceRegistry.register(helloService);
        serviceRegistry.register(morningService);

        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.start(8097);
    }

    @Test
    @DisplayName("NettyServer")
    public void testNettyServer() {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new ServiceRegistryImpl();
        serviceRegistry.register(helloService);
        NettyServer server = new NettyServer();
        server.start(9999);
    }
}
