package com.coffeerr.server.impl;

import com.coffeerr.request.RpcRequest;
import com.coffeerr.respose.RpcResponse;
import com.coffeerr.server.RpcServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/4/7 8:11 下午
 */

public class RpcServiceImpl implements RpcServerService {
    public static final Logger logger = LoggerFactory.getLogger(RpcServiceImpl.class);

    @Override
    public void threadHandler(Socket socket, Object service) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            logger.info("接收到客户端信息：" + rpcRequest.toString());
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterType());
            Object res = method.invoke(service, rpcRequest.getParameters());
            objectOutputStream.writeObject(RpcResponse.success(res));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error("" + e);
        }
    }
}
