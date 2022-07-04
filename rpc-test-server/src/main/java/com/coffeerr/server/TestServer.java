package com.coffeerr.server;

import com.alibaba.nacos.api.exception.NacosException;
import com.coffeerr.HelloService;
import com.coffeerr.MorningService;
import com.coffeerr.provider.ServiceProvider;
import com.coffeerr.provider.ServiceProviderImpl;
import com.coffeerr.registry.ServiceRegistry;
import com.coffeerr.registry.impl.NacosServiceRegistryImpl;
import com.coffeerr.registry.impl.ServiceRegistryImpl;
import com.coffeerr.serialize.CommonSerializer;
import com.coffeerr.server.impl.HelloServiceImpl;
import com.coffeerr.server.impl.MorningServiceImpl;
import com.coffeerr.server.impl.NettyServerImpl;
import com.coffeerr.server.impl.RpcServerProtocolHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/4/7 2:53 下午
 */

public class TestServer {

    @Test
    @DisplayName("服务端：Socket注册")
    public void testSocketServer() {
//        ServiceRegistry serviceRegistry = new ServiceRegistryImpl();
        ServiceProvider serviceProvider = new ServiceProviderImpl();
        HelloService helloService = new HelloServiceImpl();
        serviceProvider.addServiceProvider(helloService, HelloService.class);
        CommonSerializer commonSerializer = CommonSerializer.getSerializeByCode(2);
        RpcServer rpcServer = new RpcServer(serviceProvider, commonSerializer);
        rpcServer.start(12345);
    }

    @Test
    @DisplayName("多服务注册-服务端")
    public void testMultiServiceServer() {
        HelloService helloService = new HelloServiceImpl();
        MorningService morningService = new MorningServiceImpl();

        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8888);
//        ServiceRegistry serviceRegistry = new NacosServiceRegistryImpl(inetSocketAddress);
        ServiceProvider serviceProvider = new ServiceProviderImpl();
        serviceProvider.addServiceProvider(helloService, HelloService.class);
//        serviceRegistry.register(helloService);
//        serviceRegistry.register(morningService);
        serviceProvider.addServiceProvider(morningService, MorningService.class);
        CommonSerializer commonSerializer = CommonSerializer.getSerializeByCode(2);
        RpcServer rpcServer = new RpcServer(serviceProvider, commonSerializer);
        rpcServer.start(8097);
    }

    @Test
    @DisplayName("NettyServer")
    public void testNettyServer() throws NacosException {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new ServiceRegistryImpl();
        serviceRegistry.register(helloService);
        NettyServer server = new NettyServer();
        server.start(9999);
    }

    @Test
    @DisplayName("使用Nacos作为注册中心")
    public void testNettyServiceNacos() {
        HelloService helloService = new HelloServiceImpl();
        CommonServer nettyServer = new NettyServerImpl("127.0.0.1", 9999);
        nettyServer.setSerializer(CommonSerializer.getSerializeByCode(4));
        nettyServer.publishService(helloService, HelloService.class);
        nettyServer.start(13110);
    }
}
