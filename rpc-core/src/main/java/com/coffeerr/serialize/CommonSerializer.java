package com.coffeerr.serialize;

import com.coffeerr.serialize.impl.JsonSerializer;
import com.coffeerr.serialize.impl.KryoSerializer;

public interface CommonSerializer {
    // 默认

    public static CommonSerializer getSerializeByCode(int code) {
        switch (code) {
            case 2:
                return new KryoSerializer();
            case 1:
            default:
                return new JsonSerializer();
        }
    }


    byte[] serialize(Object object);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();
}
