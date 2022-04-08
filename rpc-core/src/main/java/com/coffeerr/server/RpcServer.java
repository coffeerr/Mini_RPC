package com.coffeerr.server;

import com.coffeerr.HelloService;
import com.coffeerr.constants.RpcError;
import com.coffeerr.exception.RpcException;
import com.coffeerr.registry.ServiceRegistry;
import com.coffeerr.registry.impl.ServiceRegistryImpl;
import com.coffeerr.request.RpcRequest;
import com.coffeerr.respose.RpcResponse;
import com.coffeerr.serialize.CommonSerializer;
import com.coffeerr.serialize.impl.Json2Serializer;
import com.coffeerr.serialize.impl.JsonSerializer;
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
import java.util.Map;
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
    private ServiceRegistry serviceRegistry;
    private CommonSerializer serializer;

    public RpcServer(ServiceRegistry serviceRegistry) {
        this.blockingQueue = new ArrayBlockingQueue<>(10);
        this.threadPoolExecutor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, blockingQueue);
        this.serviceRegistry = serviceRegistry;
        this.serializer = new Json2Serializer();
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
                threadPoolExecutor.execute(new HandlerThread(socket, serviceRegistry, serializer));
            }
        } catch (IOException e) {
            logger.info("服务器启动时有错误发生：" + e);
        } finally {
            threadPoolExecutor.shutdown();
        }
    }

//    public void register(final Object service, int port) {
//        logger.info("server 启动");
//        Socket socket;
//        try (ServerSocket serverSocket = new ServerSocket(port)) {
//            while ((socket = serverSocket.accept()) != null) {
//                logger.info("接收到地址:" + socket.getLocalSocketAddress() + "/" + socket.getLocalPort());
//                //do sth
//                final Socket finalSocket = socket;
//                threadPoolExecutor.execute(new HandlerThread(socket, serviceRegistry));
//            }
//
//        } catch (IOException e) {
//            logger.error("" + e);
//        }
//    }


}
