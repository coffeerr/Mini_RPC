package com.coffeerr.client;

import com.coffeerr.HelloObject;
import com.coffeerr.HelloService;
import com.coffeerr.MorningService;
import com.coffeerr.client.impl.NettyClient;
import com.coffeerr.serialize.CommonSerializer;
import com.coffeerr.serialize.impl.Json2Serializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/4/7 2:46 下午
 */

public class TestClient {

    @Test
    @DisplayName("客户端：Socket注册")
    public void testSocketClient() {
        CommonSerializer serializer = CommonSerializer.getSerializeByCode(2);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(12345, "127.0.0.1", serializer);
//        RpcClientProtocolHandler rpcClientProtocolHandler = new RpcClientProtocolHandler();
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello_socket = helloService.hello(new HelloObject(12, "hello Socket"));
        System.out.println("hello_socket = " + hello_socket);
    }

    @Test
    @DisplayName("多服务注册-客户端")
    public void testMultiServiceClient() {
        String host = "127.0.0.1";
        int port = 8097;
        CommonSerializer serializer = CommonSerializer.getSerializeByCode(2);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(port, host, serializer);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);

        HelloObject helloObject = new HelloObject(407, "helloService:来自客户端的问候");
        HelloObject morningHelloObject = new HelloObject(407, "morningHelloService:来自客户端的问候");

        MorningService morningService = rpcClientProxy.getProxy(MorningService.class);

        String hello = helloService.hello(helloObject);
        Assertions.assertEquals(hello, "来自服务端的招手");

        String morningHello = morningService.hello(morningHelloObject);
        Assertions.assertEquals(morningHello, "来自服务端的招手");
    }

    @Test
    @DisplayName("Netty客户端")
    public void testNettyClient() {
        String host = "127.0.0.1";
        int port = 9999;
        CommonSerializer serializer = CommonSerializer.getSerializeByCode(1);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(port, host, serializer);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject helloObject = new HelloObject(407, "helloService:来自客户端的问候");
        HelloObject morningHelloObject = new HelloObject(407, "morningHelloService:来自客户端的问候");
        MorningService morningService = rpcClientProxy.getProxy(MorningService.class);
        String hello = helloService.hello(helloObject);
        Assertions.assertEquals(hello, "来自服务端的招手");
        String hello1 = morningService.hello(morningHelloObject);
        System.out.println(hello1);
    }

    @Test
    @DisplayName("Client:引入Nacos作为注册中心")
    public void testNettyClientNacos() {
        CommonSerializer serializer = CommonSerializer.getSerializeByCode(4);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(13110, "127.0.0.1", serializer);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject helloObject = new HelloObject(407, "helloService:来自客户端的问候");
        String hello = helloService.hello(helloObject);
        Assertions.assertEquals(hello, "来自服务端的招手");
    }
}
