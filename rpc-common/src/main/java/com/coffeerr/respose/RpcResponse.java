package com.coffeerr.respose;

import com.coffeerr.constants.RpcResponseCode;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/4/7 11:16 上午
 */
@Data
@Getter
public class RpcResponse<T> implements Serializable {
    private Integer code;

    private String message;

    private T Data;

    public static <T> RpcResponse<T> success(T data) {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setCode(RpcResponseCode.SUCCESS.getCode());
        rpcResponse.setMessage(RpcResponseCode.SUCCESS.getMessage());
        rpcResponse.setData(data);
        return rpcResponse;
    }

    public static <T> RpcResponse<T> fail() {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setCode(RpcResponseCode.FAIL.getCode());
        rpcResponse.setMessage(RpcResponseCode.FAIL.getMessage());
        return rpcResponse;
    }
}
