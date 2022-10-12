package org.wx.im.websocket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wx.im.utils.StrPool;
import org.wx.im.websocket.handler.HttpRequestHandler;
import org.wx.im.websocket.handler.WebSocketServerHandler;
import org.wx.im.utils.AddressUtil;

import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * @author WangXin
 * @description Socket Server
 * @date 2022/2/23 17:36
 */
@Component
@Data
@Slf4j
@RequiredArgsConstructor
public class SocketServer {

    /**
     * 默认端口
     */
    @Value("${websocket.port}")
    private int port;

    /**
     * 最大端口
     */
    @Value("${websocket.max_port}")
    private int maxPort;

    /**
     * 通过nio方式来接收连接和处理连接
     */
    private EventLoopGroup bg;

    private EventLoopGroup wg;

    /**
     * 启动引导器
     */
    private ServerBootstrap b = new ServerBootstrap();

    private final WebSocketServerHandler webSocketServerHandler;
    private final HttpRequestHandler httpRequestHandler;

    /**
     * 启动SocketServer
     */
    public void run() {
        String ip = this.getLocalIp();
        this.assembly(ip);
        // 6 开始绑定server port
        ChannelFuture channelFuture = this.bindPort();

        // JVM关闭时的钩子函数
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    // 优雅关闭EventLoopGroup，释放掉所有资源包括创建的线程
                    wg.shutdownGracefully();
                    bg.shutdownGracefully();
                })
        );

    }

    /**
     * 获取本机ip
     *
     * @return ip address
     */
    private String getLocalIp() {
        String ip;
        try {
            ip = AddressUtil.getHostIp();
            if (ip == null) {
                ip = "127.0.0.1";
            }
        } catch (SocketException e) {
            log.warn("获取IP地址异常：{}", e.getMessage());
            ip = "127.0.0.1";
        }
        return ip;
    }

    /**
     * 绑定可用端口
     */
    private ChannelFuture bindPort() {
        ChannelFuture channelFuture = null;
        boolean isStart = false;
        while (!isStart) {
            try {
                channelFuture = b.bind().sync();
                log.info("IM-Server启动成功, 连接 ws://{}", channelFuture.channel().localAddress().toString().replace("/", StrPool.EMPTY));
                isStart = true;
            } catch (Exception e) {
                log.error("发生启动异常", e);
                if (port > maxPort) {
                    // 一直到最大端口也没有启动成功，则不再运行
                    throw new RuntimeException("7000~8000范围内无可用端口");
                }
                port++;
                log.info("尝试一个新的端口：{}", port);
                b.localAddress(new InetSocketAddress(port));
            }
        }
        return channelFuture;
    }

    /**
     * 执行装配流程
     */
    private void assembly(String ip) {
        //连接监听线程组
        bg = new NioEventLoopGroup(1);
        //传输处理线程组
        wg = new NioEventLoopGroup();
        //1 设置reactor 线程
        b.group(bg, wg);
        //2 设置nio类型的channel
        b.channel(NioServerSocketChannel.class);
        //3 设置监听端口
        b.localAddress(new InetSocketAddress(ip, port));
        //4 设置通道选项
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        //5 装配流水线
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            //有连接到达时会创建一个channel
            @Override
            protected void initChannel(SocketChannel ch) {
                // 管理pipeline中的Handler
                //设置log监听器，并且日志级别为debug，方便观察运行流程
                ch.pipeline().addLast("logging", new LoggingHandler("DEBUG"));
                //设置解码器
                ch.pipeline().addLast("http-codec", new HttpServerCodec());
                //聚合器，使用websocket会用到 把HTTP头、HTTP体拼成完整的HTTP请求
                ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
                //用于大数据的分区传输
                ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                ch.pipeline().addLast(new WebSocketServerProtocolHandler("/", null, true, 65536 * 10));
                // 用于http 升级成 websocket
                ch.pipeline().addLast("http-handler", httpRequestHandler);
                //自定义的业务handler 处理websocket
                ch.pipeline().addLast("handler", webSocketServerHandler);
            }
        });
    }

}
