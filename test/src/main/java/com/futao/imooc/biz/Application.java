package com.futao.imooc.biz;

import com.futao.imooc.web.server.TomcatServer;
import org.apache.catalina.LifecycleException;

/**
 * @author futao
 * @date 2020/2/29.
 */
public class Application {
    public static void main(String[] args) throws LifecycleException {
        new TomcatServer().startServer("com.futao.imooc");
    }
}
