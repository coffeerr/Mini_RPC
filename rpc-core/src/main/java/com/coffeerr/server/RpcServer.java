package com.coffeerr.server;

import com.coffeerr.constants.RpcError;
import com.coffeerr.exception.RpcException;
import com.coffeerr.provider.ServiceProvider;
import com.coffeerr.registry.ServiceRegistry;
import com.coffeerr.serialize.CommonSerializer;
import com.coffeerr.serialize.impl.Json2Serializer;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/4/7 2:04 下午
 */
@Data
public class RpcServer {
    public static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    private BlockingQueue<Runnable> blockingQueue;
    private ThreadPoolExecutor threadPoolExecutor;
//    private ServiceRegistry serviceRegistry;

    private ServiceProvider serviceProvider;
    private CommonSerializer serializer;

    public RpcServer(ServiceProvider serviceProvider, CommonSerializer serializer) {
        this.blockingQueue = new ArrayBlockingQueue<>(10);
        this.threadPoolExecutor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, blockingQueue);
        this.serviceProvider = serviceProvider;
        this.serializer = serializer;
    }

    void start(int port) {
        if (serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("服务器启动……");
            Socket socket;
            //当未接收到连接请求时，accept()会一直阻塞
            while ((socket = serverSocket.accept()) != null) {
                logger.info("客户端连接！{}:{}", socket.getInetAddress(), socket.getPort());
                threadPoolExecutor.execute(new HandlerThread(socket, serviceProvider, serializer));
            }
        } catch (IOException e) {
            logger.info("服务器启动时有错误发生：" + e);
        } finally {
            threadPoolExecutor.shutdown();
        }
    }


}
