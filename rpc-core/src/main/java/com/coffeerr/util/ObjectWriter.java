package com.coffeerr.util;

import com.coffeerr.constants.PackageType;
import com.coffeerr.request.RpcRequest;
import com.coffeerr.serialize.CommonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

public class ObjectWriter {

    private static final Logger logger = LoggerFactory.getLogger(ObjectWriter.class);
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    public static void writeObject(OutputStream out, Object object, CommonSerializer serializer) throws IOException {
        out.write(intToBytes(MAGIC_NUMBER));
        if (object instanceof RpcRequest) {
            out.write(intToBytes(PackageType.REQUEST_PACK.getPackageCode()));
        } else {
            out.write(intToBytes(PackageType.RESPONSE_PACK.getPackageCode()));
        }
        out.write(intToBytes(1));
        byte[] bytes = serializer.serialize(object);
        out.write(intToBytes(bytes.length));
        out.write(bytes);
        out.flush();
    }

    /**
     * @param value
     * @return [byte[]]
     * @description 将Int转换为字节数组
     * @date [2021-03-10 22:15]
     */
    private static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }
}
