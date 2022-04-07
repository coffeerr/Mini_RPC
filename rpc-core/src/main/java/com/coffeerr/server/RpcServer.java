package com.coffeerr.server;

import com.coffeerr.HelloService;
import com.coffeerr.request.RpcRequest;
import com.coffeerr.respose.RpcResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/4/7 2:04 下午
 */
@Data
public class RpcServer {
    public static final Logger logger = LoggerFactory.getLogger(RpcServer.class);
    public static BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(10);
    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, blockingQueue);

    public void register(final Object service, int port) {
        logger.info("server 启动");
        Socket socket;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while ((socket = serverSocket.accept()) != null) {
                logger.info("接收到地址:" + socket.getLocalSocketAddress() + "/" + socket.getLocalPort());
                //do sth
                final Socket finalSocket = socket;
                threadPoolExecutor.execute(new HandlerThread(service, socket));
            }

        } catch (IOException e) {
            logger.error("" + e);
        }
    }


}
