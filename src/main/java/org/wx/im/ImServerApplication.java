package org.wx.im;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.wx.im.websocket.server.SocketServer;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 聊天服务启动器
 *
 * @author WangXin
 **/
@SpringBootApplication
@MapperScan("org.wx.im.dao")
@Slf4j
public class ImServerApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext context = SpringApplication.run(ImServerApplication.class, args);
        Environment env = context.getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "应用 '{}' 启动成功! 访问连接：\n\t" +
                        "在线接口文档：http://{}:{}/doc.html\n" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port") + env.getProperty("server.servlet.context-path")
        );

        // 初始化SocketServer
        SocketServer socketServer = context.getBean(SocketServer.class);
        socketServer.run();
    }

}
