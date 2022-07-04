package com.coffeerr.client.socket;

import com.coffeerr.client.RpcClientService;
import com.coffeerr.constants.RpcError;
import com.coffeerr.exception.RpcException;
import com.coffeerr.request.RpcRequest;
import com.coffeerr.respose.RpcResponse;
import com.coffeerr.serialize.CommonSerializer;
import com.coffeerr.util.ObjectReader;
import com.coffeerr.util.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.LongAdder;

/**
 * @description: 通过协议实现传输
 * @author: Desmond
 * @time: 2022/4/7 6:10 下午
 */

public class RpcClientProtocolHandler implements RpcClientService {
    public static final Logger logger = LoggerFactory.getLogger(RpcClientProtocolHandler.class);

    public static LongAdder longAdder = new LongAdder();

//    @Override
//    public Object sendRequest(RpcRequest rpcRequest, String host, int port, CommonSerializer serializer) {
//        Object res = null;
//        try (Socket socket = new Socket(host, port)) {
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
//            // ｜魔术位 ｜ 长度 ｜ 消息ID ｜ 协议版本 ｜ 消息类型 ｜ 序列化方式｜数据｜
//            // 1、写入魔数
//            objectOutputStream.write(DataTransportUtils.intConverteByteArray(ProtocolConstants.MAGIC_NUMBER));
//
//            // 2、长度
//            byte[] bytes = serializer.serialize(rpcRequest);
//            int length = bytes.length;
//            objectOutputStream.write(DataTransportUtils.intConverteByteArray(length));
//
//            // 3、消息ID
//            longAdder.increment();
//            int messageId = longAdder.intValue();
//            objectOutputStream.write(DataTransportUtils.intConverteByteArray(messageId));
//
//            // 4、协议版本
//            objectOutputStream.write(DataTransportUtils.intConverteByteArray(ProtocolConstants.INIT_VERSION));
//
//            // 5、协议类型
//            objectOutputStream.write(DataTransportUtils.intConverteByteArray(PackageType.REQUEST_PACK.getPackageCode()));
//
//            // 6、序列化方式
//            objectOutputStream.write(DataTransportUtils.intConverteByteArray(SerializerCode.JSON.getCode()));
//
//            // 7、数据
//            objectOutputStream.write(bytes);
//
//            objectOutputStream.flush();
//            res = objectInputStream.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//            logger.error("" + e);
//        }
//        return res;
//    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest, String host, int port, CommonSerializer serializer) {
        if (serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        /**
         * socket套接字实现TCP网络传输
         * try()中一般放对资源的申请，若{}出现异常，()资源会自动关闭
         */
        try (Socket socket = new Socket(host, port)) {
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream, rpcRequest, serializer);
            Object obj = ObjectReader.readObject(inputStream, serializer);
            RpcResponse rpcResponse = (RpcResponse) obj;
            return rpcResponse.getData();
        } catch (IOException e) {
            logger.error("调用时有错误发生：" + e);
            throw new RpcException("服务调用失败：", e);
        }
    }
}
