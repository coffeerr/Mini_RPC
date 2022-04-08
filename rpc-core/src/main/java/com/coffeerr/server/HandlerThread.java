package com.coffeerr.server;

import com.coffeerr.constants.PackageType;
import com.coffeerr.constants.ProtocolConstants;
import com.coffeerr.constants.RpcError;
import com.coffeerr.exception.RpcException;
import com.coffeerr.registry.ServiceRegistry;
import com.coffeerr.request.RpcRequest;
import com.coffeerr.respose.RpcResponse;
import com.coffeerr.serialize.CommonSerializer;
import com.coffeerr.server.impl.RpcServiceImpl;
import com.coffeerr.util.ObjectReader;
import com.coffeerr.util.ObjectWriter;
import com.coffeerr.utils.DataTransportUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * @description: 用来处理客户端消息的线程对象
 * @author: Desmond
 * @time: 2022/4/7 5:31 下午
 */
@Data
public class HandlerThread implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(HandlerThread.class);

    private Socket socket;
    private ServiceRegistry serviceRegistry;
    private CommonSerializer serializer;

    public HandlerThread(Socket socket, ServiceRegistry serviceRegistry, CommonSerializer serializer) {
        this.socket = socket;
        this.serializer = serializer;
        this.serviceRegistry = serviceRegistry;
    }

//    @Override
//    public void run() {
//        try {
//            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//
//
//            byte[] bytes = new byte[4];
//            // ｜魔术位 ｜ 长度 ｜ 消息ID ｜ 协议版本 ｜ 消息类型 ｜ 序列化方式｜数据｜
//            objectInputStream.read(bytes);
//            int magic = DataTransportUtils.byteArrayConvertInt(bytes);
//            if (magic != ProtocolConstants.MAGIC_NUMBER) {
//                System.out.println("接收到魔数：" + magic);
//                throw new RpcException(RpcError.MAGIC_NUMBER_ERROR);
//            }
//
//
//            objectInputStream.read(bytes);
//            int length = DataTransportUtils.byteArrayConvertInt(bytes);
//
//            objectInputStream.read(bytes);
//            int messageId = DataTransportUtils.byteArrayConvertInt(bytes);
//
//            objectInputStream.read(bytes);
//            int protocolVersion = DataTransportUtils.byteArrayConvertInt(bytes);
//            if (protocolVersion != ProtocolConstants.INIT_VERSION) {
//                throw new RpcException(RpcError.PROTOCOL_VERSION_ERROR);
//            }
//
//            Class<?> packageClass;
//
//            objectInputStream.read(bytes);
//            int packageType = DataTransportUtils.byteArrayConvertInt(bytes);
//            Object ob;
//            if (packageType == PackageType.REQUEST_PACK.getPackageCode()) {
//                packageClass = RpcRequest.class;
//            } else if (packageType == PackageType.RESPONSE_PACK.getPackageCode()) {
//                packageClass = RpcResponse.class;
//            } else {
//                throw new RpcException(RpcError.PACKAGE_TYPE_ERROR);
//            }
//
//
//            objectInputStream.read(bytes);
//            int serializerType = DataTransportUtils.byteArrayConvertInt(bytes);
//            CommonSerializer commonSerializer = serializer;
//
//
//            byte[] data = new byte[length];
//            objectInputStream.read(data);
//
//            Object deserialize = commonSerializer.deserialize(data, packageClass);
//
//            if (packageType == PackageType.REQUEST_PACK.getPackageCode()) {
//                RpcRequest rpcRequest = (RpcRequest) deserialize;
//                logger.info("接收到客户端信息：" + rpcRequest.toString());
//                Object service = serviceRegistry.getService(rpcRequest.getInterfaceName());
//
//
//
//                Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterType());
//
//
//                Object res = method.invoke(service, rpcRequest.getParameters());
//                objectOutputStream.writeObject(RpcResponse.success(res));
//                objectOutputStream.flush();
//            }
//
//
//        } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            logger.error("" + e);
//        }
//    }


    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);

            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterType());

            Object response = RpcResponse.success(method.invoke(service, rpcRequest.getParameters()));
            ObjectWriter.writeObject(outputStream, response, serializer);
        } catch (Exception e) {
            logger.error("" + e);
        }
    }

}
