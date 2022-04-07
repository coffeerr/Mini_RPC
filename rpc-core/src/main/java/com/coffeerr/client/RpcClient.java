package com.coffeerr.client;

import com.coffeerr.request.RpcRequest;
import com.coffeerr.respose.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/4/7 2:04 下午
 */

public class RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    /**
     * 发送请求
     *
     * @param rpcRequest
     * @return
     */
    public Object sendRequest(RpcRequest rpcRequest, int port, String host) {
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
