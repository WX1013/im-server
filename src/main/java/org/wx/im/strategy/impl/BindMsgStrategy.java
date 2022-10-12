package org.wx.im.strategy.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.wx.im.strategy.MsgStrategy;
import org.wx.im.websocket.connection.Connection;
import org.wx.im.websocket.connection.UserSession;
import org.wx.im.websocket.entity.Message;

import static org.wx.im.strategy.impl.BindMsgStrategy.MSG_TYPE;


/**
 * @author WangXin
 * @description 绑定消息
 * @date 2022/3/25 17:24
 */
@Slf4j
@Component(MSG_TYPE)
@RequiredArgsConstructor
public class BindMsgStrategy implements MsgStrategy {

    public static final String MSG_TYPE = "BIND";

    private final UserSession userSession;

    /**
     * 处理消息
     *
     * @param message 消息对象
     * @param conn    当前连接
     */
    @Override
    public void handleMsg(Message message, Connection conn) {
        // userId -> sessionId 进行绑定，存入缓存
        userSession.addSessionId(message.getFrom().getUid(), conn.getSessionId());

        // 发送给绑定方
        conn.send(message);
    }
}
