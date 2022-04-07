package com.coffeerr.server.impl;

import com.coffeerr.constants.PackageType;
import com.coffeerr.constants.ProtocolConstants;
import com.coffeerr.constants.RpcError;
import com.coffeerr.constants.SerializerCode;
import com.coffeerr.exception.RpcException;
import com.coffeerr.request.RpcRequest;
import com.coffeerr.respose.RpcResponse;
import com.coffeerr.serialize.CommonSerializer;
import com.coffeerr.serialize.impl.JsonSerializer;
import com.coffeerr.server.RpcServerService;
import com.coffeerr.utils.DataTransportUtils;
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
 * @time: 2022/4/7 8:15 下午
 */

public class RpcServerProtocolHandler implements RpcServerService {
    public static final Logger logger = LoggerFactory.getLogger(RpcServerProtocolHandler.class);

    @Override
    public void threadHandler(Socket socket, Object service) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());


            byte[] bytes = new byte[4];
            // ｜魔术位 ｜ 长度 ｜ 消息ID ｜ 协议版本 ｜ 消息类型 ｜ 序列化方式｜数据｜
            objectInputStream.read(bytes);
            int magic = DataTransportUtils.byteArrayConvertInt(bytes);
            if (magic != ProtocolConstants.MAGIC_NUMBER) {
                throw new RpcException(RpcError.MAGIC_NUMBER_ERROR);
            }


            objectInputStream.read(bytes);
            int length = DataTransportUtils.byteArrayConvertInt(bytes);

            objectInputStream.read(bytes);
            int messageId = DataTransportUtils.byteArrayConvertInt(bytes);

            objectInputStream.read(bytes);
            int protocolVersion = DataTransportUtils.byteArrayConvertInt(bytes);
            if (protocolVersion != ProtocolConstants.INIT_VERSION) {
                throw new RpcException(RpcError.PROTOCOL_VERSION_ERROR);
            }

            Class<?> packageClass;

            objectInputStream.read(bytes);
            int packageType = DataTransportUtils.byteArrayConvertInt(bytes);
            Object ob;
            if (packageType == PackageType.REQUEST_PACK.getPackageCode()) {
                packageClass = RpcRequest.class;
            } else if (packageType == PackageType.RESPONSE_PACK.getPackageCode()) {
                packageClass = RpcResponse.class;
            } else {
                throw new RpcException(RpcError.PACKAGE_TYPE_ERROR);
            }


            objectInputStream.read(bytes);
            int serializerType = DataTransportUtils.byteArrayConvertInt(bytes);
            CommonSerializer commonSerializer = CommonSerializer.getSerializeByCode(serializerType);


            byte[] data = new byte[length];
            objectInputStream.read(data);

            Object deserialize = commonSerializer.deserialize(data, packageClass);

            if (packageType == PackageType.REQUEST_PACK.getPackageCode()) {
                RpcRequest rpcRequest = (RpcRequest) deserialize;
                logger.info("接收到客户端信息：" + rpcRequest.toString());
                Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterType());
                Object res = method.invoke(service, rpcRequest.getParameters());
                objectOutputStream.writeObject(RpcResponse.success(res));
                objectOutputStream.flush();
            }


        } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error("" + e);
        }
    }
}
