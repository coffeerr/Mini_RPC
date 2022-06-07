package com.coffeerr.exception;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/6/7 4:00 下午
 */

public class DeserializeException extends RuntimeException {
    public DeserializeException(String msg) {
        super(msg);
    }
}
