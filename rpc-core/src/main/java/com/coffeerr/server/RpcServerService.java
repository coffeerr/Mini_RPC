package com.coffeerr.server;

import java.net.Socket;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/4/7 8:10 下午
 */

public interface RpcServerService {
    void threadHandler(Socket socket, Object service);
}
