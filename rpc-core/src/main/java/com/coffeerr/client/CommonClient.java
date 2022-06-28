package com.coffeerr.client;

import com.coffeerr.request.RpcRequest;
import com.coffeerr.serialize.CommonSerializer;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/6/28 10:52 AM
 */

public interface CommonClient {
    /**
     * 发送请求
     *
     * @param rpcRequest
     * @return
     */
    Object sendRequest(RpcRequest rpcRequest);

    void setSerializer(CommonSerializer commonSerializer);
}
