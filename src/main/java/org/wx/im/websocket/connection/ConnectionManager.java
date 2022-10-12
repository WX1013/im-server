package org.wx.im.websocket.connection;

import cn.hutool.core.util.IdUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.wx.im.utils.StrPool;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 储存所有连接
 *
 * @author Joker
 * @date 2022/1/6
 */
@Slf4j
public class ConnectionManager {

    public static ConcurrentHashMap<String, Connection> connectionMap = new ConcurrentHashMap<>();

    /**
     * 获取connection
     *
     * @param ctx
     * @return
     */
    public static Connection getConnection(ChannelHandlerContext ctx) {
        Session session = ctx.channel().attr(Connection.SESSION).get();
        Connection conn = connectionMap.get(session.getId());
        if (conn != null) {
            return conn;
        } else {
            log.error("Connection not found in sessionMap, session: {}", session);
        }
        return null;
    }

    /**
     * 根据sessionId获取Connection
     *
     * @param sessionId
     * @return
     */
    public static Connection getConnection(String sessionId) {
        Connection conn = connectionMap.get(sessionId);
        if (conn != null) {
            return conn;
        } else {
            log.error("Connection not found in sessionMap, sessionId: {}", sessionId);
        }
        return null;
    }

    /**
     * 建立连接，添加connection
     *
     * @param c
     */
    public static void addConnection(ChannelHandlerContext c) {
        String sessionId = getSessionId();
        while (connectionMap.containsKey(sessionId)) {
            sessionId = getSessionId();
        }

        Connection conn = new Connection(c, new Session(sessionId));
        connectionMap.put(sessionId, conn);
        log.info("建立连接：SessionId={}, Connection={}, onlineUserCount: {}", sessionId, conn, connectionMap.size());
    }

    /**
     * 删除连接
     *
     * @param c
     */
    public static void removeConnection(ChannelHandlerContext c) {
        Connection conn = getConnection(c);
        if (conn != null) {
            Session session = conn.getSession();
            String sessionId = session.getId();
            if (ConnectionManager.connectionMap.remove(sessionId) != null) {
                try {
                    conn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                log.error("Session: {} is not exist in sessionMap", session);
            }
            log.info("Client disconnected, Session: {}", session.getId());
        }
    }

    /**
     * 生成sessionId
     *
     * @return sessionId
     */
    protected static String getSessionId() {
        return IdUtil.fastUUID().replaceAll(StrPool.DASH, StrPool.EMPTY);
    }
}
