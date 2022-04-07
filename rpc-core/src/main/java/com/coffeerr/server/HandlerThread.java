package com.coffeerr.server;

import com.coffeerr.request.RpcRequest;
import com.coffeerr.respose.RpcResponse;
import com.coffeerr.server.impl.RpcServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @description: 用来处理客户端消息的线程对象
 * @author: Desmond
 * @time: 2022/4/7 5:31 下午
 */
@Data
@AllArgsConstructor
public class HandlerThread implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(HandlerThread.class);

    private Object service;
    private Socket socket;

    @Override
    public void run() {
        RpcServerService rpcServerService = new RpcServiceImpl();
        rpcServerService.threadHandler(socket, service);
    }
}
