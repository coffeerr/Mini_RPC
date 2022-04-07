package com.coffeerr.server;

import com.coffeerr.HelloService;
import com.coffeerr.server.impl.HelloServiceImpl;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/4/7 2:53 下午
 */

public class TestServer {
    public static void main(String[] args) {
        RpcServer rpcServer = new RpcServer();
        HelloService helloService = new HelloServiceImpl();
        rpcServer.register(helloService, 8097);
    }
}
