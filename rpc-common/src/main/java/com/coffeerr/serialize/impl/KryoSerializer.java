package com.coffeerr.serialize.impl;

import com.coffeerr.constants.SerializeCode;
import com.coffeerr.exception.DeserializeException;
import com.coffeerr.exception.SerializeException;
import com.coffeerr.request.RpcRequest;
import com.coffeerr.respose.RpcResponse;
import com.coffeerr.serialize.CommonSerializer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.sql.rowset.serial.SerialException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/6/7 3:25 下午
 */

public class KryoSerializer implements CommonSerializer {
    public static final Logger logger = LoggerFactory.getLogger(KryoSerializer.class);


    //使用ThreadLocal初始化Kryo，因为Kryo中的output和input是线程不安全的
    public static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RpcRequest.class);
        kryo.register(RpcResponse.class);
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public byte[] serialize(Object object) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, object);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerializeException("序列化时发生错误");
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = new Kryo();
            Object o = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return o;
        } catch (IOException e) {
            e.printStackTrace();
            throw new DeserializeException("反序列化时发生错误");
        }
    }

    @Override
    public int getCode() {
        return SerializeCode.KRYO.getCode();
    }
}
