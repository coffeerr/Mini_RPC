package com.coffeerr.exception;

import com.coffeerr.constants.RpcError;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/4/7 8:21 下午
 */
public class RpcException extends RuntimeException {

    public RpcException(RpcError error, String detail) {
        super(error.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcError error) {
        super(error.getMessage());
    }
}
