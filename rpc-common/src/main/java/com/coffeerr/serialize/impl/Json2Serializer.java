package com.coffeerr.serialize.impl;

import com.coffeerr.constants.RpcError;
import com.coffeerr.exception.RpcException;
import com.coffeerr.request.RpcRequest;
import com.coffeerr.serialize.CommonSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author [PANDA] 1843047930@qq.com
 * @date [2021-02-22 14:33]
 * @description 使用Json格式的序列化器
 */
public class Json2Serializer implements CommonSerializer {

    private static final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);
    //ObjectMapper支持线程安全
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            logger.error("序列化时有错误发生：{}", e.getMessage());
            throw new RpcException(RpcError.SERIALIZE_ERROR);
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try {
            Object obj = objectMapper.readValue(bytes, clazz);
            if (obj instanceof RpcRequest) {
                obj = handleRequest(obj);
            }
            return obj;
        } catch (IOException e) {
            logger.error("反序列化时有错误发生：{}", e.getMessage());
            throw new RpcException(RpcError.DESERIALIZE_ERROR);
        }
    }

    @Override
    public int getCode() {
        return 1;
    }

    /**
     * @param obj
     * @return [java.lang.Object]
     * @description 使用JSON反序列化Object数组，无法保证反序列化后仍然为原实例类，通常直接被反序列化成String类型，因此要特殊处理
     * @date [2021-02-22 15:03]
     */
    private Object handleRequest(Object obj) throws IOException {
        RpcRequest rpcRequest = (RpcRequest) obj;
        for (int i = 0; i < rpcRequest.getParameterType().length; i++) {
            Class<?> clazz = rpcRequest.getParameterType()[i];
            if (!clazz.isAssignableFrom(rpcRequest.getParameters()[i].getClass())) {
                byte[] bytes = objectMapper.writeValueAsBytes(rpcRequest.getParameters()[i]);
                rpcRequest.getParameters()[i] = objectMapper.readValue(bytes, clazz);
            }
        }
        return rpcRequest;
    }

}
