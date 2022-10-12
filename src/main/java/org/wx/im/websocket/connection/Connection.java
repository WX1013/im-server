package org.wx.im.websocket.connection;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.wx.im.websocket.entity.Message;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Joker
 * @date 2022/1/6
 */
@Slf4j
public class Connection implements Closeable {

    public static AttributeKey<Session> SESSION = AttributeKey.valueOf("session");

    private final Session session;

    private ChannelHandlerContext ctx;

    Connection(ChannelHandlerContext c, Session session) {
        if (c == null || session == null) {
            throw new IllegalArgumentException("ChannelHandlerContext and Session can not be null.");
        }
        this.ctx = c;
        this.session = session;
        this.ctx.channel().attr(Connection.SESSION).set(session);
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public Session getSession() {
        return this.session;
    }

    public String getSessionId() {
        if (this.session == null) {
            return null;
        } else {
            return this.session.getId();
        }
    }

    @Override
    public void close() throws IOException {
        if (ctx.channel().isActive()) {
            ctx.close();
        }
    }

    /**
     * 执行发送消息
     *
     * @param message message
     */
    public void send(Message message) {
        String m = JSON.toJSONString(message);
        log.info("Send chat message to sessionId={}, message={}", session.getId(), m);
        this.ctx.channel().writeAndFlush(new TextWebSocketFrame(m));
    }

    @Override
    public String toString() {
        return "Connection{" + "session=" + session +
                ", ctx=" + ctx +
                '}';
    }
}
