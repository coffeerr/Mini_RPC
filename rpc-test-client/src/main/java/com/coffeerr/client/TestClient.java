package com.coffeerr.client;

import com.coffeerr.HelloObject;
import com.coffeerr.HelloService;
import com.coffeerr.respose.RpcResponse;

import java.lang.reflect.Proxy;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/4/7 2:46 下午
 */

public class TestClient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 8097;

        RpcClientProxy rpcClientProxy = new RpcClientProxy(port, host);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);

        HelloObject helloObject = new HelloObject(407, "来自客户端的问候");

        String hello = helloService.hello(helloObject);
        System.out.println(hello);
    }
}
