package com.coffeerr.client;

import com.coffeerr.request.RpcRequest;
import com.coffeerr.serialize.CommonSerializer;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/4/7 6:12 下午
 */

public interface RpcClientService {
    /**
     * 发送请求
     *
     * @param rpcRequest
     * @param host
     * @param port
     * @return
     */
    Object sendRequest(RpcRequest rpcRequest, String host, int port, CommonSerializer serializer);
}
