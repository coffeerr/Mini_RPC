package com.coffeerr.client.impl;

import com.coffeerr.client.RpcClientService;
import com.coffeerr.constants.ProtocolConstants;
import com.coffeerr.request.RpcRequest;
import com.coffeerr.utils.DataTransportUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @description: 通过协议实现传输
 * @author: Desmond
 * @time: 2022/4/7 6:10 下午
 */

public class RpcClientProtocolHandler implements RpcClientService {
    public static final Logger logger = LoggerFactory.getLogger(RpcClientProtocolHandler.class);

    @Override
    public Object sendRequest(RpcRequest rpcRequest, String host, int port) {
        Object res = null;
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // ｜魔术位 ｜ 长度 ｜ 消息ID ｜ 协议版本 ｜ 消息类型 ｜ 序列化方式｜数据｜
            // 1、写入魔数
            objectOutputStream.write(DataTransportUtils.intConverteByteArray(ProtocolConstants.MAGIC_NUMBER));
            // 2、长度


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
