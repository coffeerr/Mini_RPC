package com.coffeerr.client.impl;

import com.alibaba.fastjson.JSON;
import com.coffeerr.client.RpcClientService;
import com.coffeerr.constants.PackageType;
import com.coffeerr.constants.ProtocolConstants;
import com.coffeerr.constants.SerializerCode;
import com.coffeerr.request.RpcRequest;
import com.coffeerr.utils.DataTransportUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.LongAdder;

/**
 * @description: 通过协议实现传输
 * @author: Desmond
 * @time: 2022/4/7 6:10 下午
 */

public class RpcClientProtocolHandler implements RpcClientService {
    public static final Logger logger = LoggerFactory.getLogger(RpcClientProtocolHandler.class);

    public static LongAdder longAdder;

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
            byte[] bytes = JSON.toJSONBytes(rpcRequest);
            int length = bytes.length;
            objectOutputStream.write(DataTransportUtils.intConverteByteArray(length));

            // 3、消息ID
            longAdder.increment();
            int messageId = longAdder.intValue();
            objectOutputStream.write(DataTransportUtils.intConverteByteArray(messageId));

            // 4、协议版本
            objectOutputStream.write(DataTransportUtils.intConverteByteArray(ProtocolConstants.INIT_VERSION));

            // 5、协议类型
            objectOutputStream.write(DataTransportUtils.intConverteByteArray(PackageType.REQUEST_PACK.getPackageCode()));

            // 6、序列化方式
            objectOutputStream.write(DataTransportUtils.intConverteByteArray(SerializerCode.JSON.getCode()));

            // 7、数据
            objectOutputStream.write(bytes);

            objectOutputStream.flush();
            res = objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("" + e);
        }
        return res;
    }
}
