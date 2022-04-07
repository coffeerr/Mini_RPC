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
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        handleRequest(service, finalSocket);
                    }
                });
            }

        } catch (IOException e) {
            logger.error("" + e);
        }
    }

    public static void handleRequest(Object service, Socket socket) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            logger.info("接收到客户端信息：" + rpcRequest.toString());

            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterType());
            Object res = method.invoke(service, rpcRequest.getParameters());

            objectOutputStream.writeObject(RpcResponse.success(res));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error("" + e);
        }
    }
}
