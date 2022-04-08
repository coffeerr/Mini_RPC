package com.coffeerr.client.impl;

import com.coffeerr.client.RpcClientService;
import com.coffeerr.request.RpcRequest;
import com.coffeerr.serialize.CommonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @description: 简单传输
 * @author: Desmond
 * @time: 2022/4/7 2:04 下午
 */

public class RpcClient implements RpcClientService {
    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    /**
     * 发送请求
     *
     * @param rpcRequest
     * @return
     */
    @Override
    public Object sendRequest(RpcRequest rpcRequest, String host, int port, CommonSerializer serializer) {
        Object res = null;
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            res = objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("" + e);
        }
        return res;
    }
}
