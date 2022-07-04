package com.coffeerr.serialize;

import com.coffeerr.serialize.impl.HessianSerializer;
import com.coffeerr.serialize.impl.JsonSerializer;
import com.coffeerr.serialize.impl.KryoSerializer;
import com.coffeerr.serialize.impl.ProtostuffSerializer;

public interface CommonSerializer {
    // 默认
    static CommonSerializer getSerializeByCode(int code) {
        switch (code) {
            case 1:
                return new JsonSerializer();
            case 2:
                return new KryoSerializer();
            case 3:
                return new HessianSerializer();
            case 4:
                return new ProtostuffSerializer();

            default:
                return new JsonSerializer();
        }
    }


    byte[] serialize(Object object);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();
}
