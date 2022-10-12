package org.wx.im.websocket.handler;

import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.wx.im.websocket.connection.Connection;
import org.wx.im.websocket.connection.ConnectionManager;
import org.wx.im.websocket.entity.Message;
import org.wx.im.strategy.MsgContext;

/**
 * @author WangXin
 * @date 2022/7/3 22:26
 */
@Slf4j
@Service
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class WebSocketServerHandler extends ChannelInboundHandlerAdapter {

    private final MsgContext msgContext;

    /**
     * 读取消息
     *
     * @param ctx   the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *              belongs to
     * @param frame the message to handle
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object frame) throws Exception {
        // 关闭请求
        if (frame instanceof CloseWebSocketFrame) {
            ctx.close();
            return;
        }
        // ping请求
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(((PingWebSocketFrame) frame).content().retain()));
            return;
        }
        // 只支持文本格式，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            sendErrorMessage(ctx, "仅支持文本(Text)格式，不支持二进制消息");
            return;
        }

        Connection conn = ConnectionManager.getConnection(ctx);
        if (conn == null) {
            return;
        }

        // 客户端发送过来的消息
        String msg = ((TextWebSocketFrame) frame).text();
        log.info("服务端收到新信息：" + msg);
        Message messageObj = transfer(msg);
        if (messageObj == null) {
            sendErrorMessage(ctx, "消息格式错误");
            return;
        }

        msgContext.getStrategy(messageObj.getMsgType()).handleMsg(messageObj, conn);
    }

    /**
     * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    /**
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     *
     * @param ctx ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("有客户端链接成功");
        ConnectionManager.addConnection(ctx);
    }


    /**
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     *
     * @param ctx ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ConnectionManager.removeConnection(ctx);
    }

    /**
     * 发送错误信息
     *
     * @param ctx ctx
     * @param msg msg
     */
    private void sendErrorMessage(ChannelHandlerContext ctx, String msg) {
        ctx.channel().writeAndFlush(new TextWebSocketFrame(msg));
    }

    /**
     * 消息转换
     *
     * @param msg
     * @return Message
     * @author WangXin
     * @date 2022/10/9 23:25
     **/
    private Message transfer(String msg) {
        try {
            return JSONObject.parseObject(msg, Message.class);
        } catch (Exception e) {
            log.error("Websocket接收到的消息格式错误: {}", msg);
            return null;
        }
    }

}
