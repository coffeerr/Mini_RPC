package com.coffeerr.codec;

import com.coffeerr.constants.PackageType;
import com.coffeerr.constants.SerializeCode;
import com.coffeerr.request.RpcRequest;
import com.coffeerr.serialize.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @description: 通用编码拦截器
 * @author: Desmond
 * @time: 2022/6/6 3:02 下午
 */

public class CommonEncoder extends MessageToByteEncoder {
    private CommonSerializer commonSerializer;
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    public CommonEncoder(CommonSerializer commonSerializer) {
        this.commonSerializer = commonSerializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object object, ByteBuf out) throws Exception {
        out.writeInt(MAGIC_NUMBER);
        if (object instanceof RpcRequest) {
            out.writeInt(PackageType.REQUEST_PACK.getPackageCode());
        } else {
            out.writeInt(PackageType.RESPONSE_PACK.getPackageCode());
        }
        out.writeInt(commonSerializer.getCode());
        byte[] bytes = commonSerializer.serialize(object);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
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
