package com.coffeerr.hook;


import com.coffeerr.utils.NacosUtil;
import lombok.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 自定义钩子
 * @author: Desmond
 * @time: 2022/6/27 5:39 PM
 */

public class ShutDownHook {
    public static final Logger logger = LoggerFactory.getLogger(ShutDownHook.class);
    private static volatile ShutDownHook shutDownHook = null;

    private ShutDownHook() {
    }

    ;

    public static ShutDownHook getInstance() {
        if (null == shutDownHook) {
            synchronized (ShutDownHook.class) {
                if (null == shutDownHook) {
                    shutDownHook = new ShutDownHook();
                }
            }
        }
        return shutDownHook;
    }

    public void addClearAllHook() {
        logger.info("服务端关闭前将自动注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtil.clearRegistry();
//            threadPool.shutdown();
        }));
    }
}
