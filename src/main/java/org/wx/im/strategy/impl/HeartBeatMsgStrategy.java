package org.wx.im.strategy.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.wx.im.strategy.MsgStrategy;
import org.wx.im.websocket.connection.Connection;
import org.wx.im.websocket.entity.Message;

import static org.wx.im.strategy.impl.HeartBeatMsgStrategy.MSG_TYPE;


/**
 * @author WangXin
 * @description 心跳消息
 * @date 2022/3/25 17:24
 */
@Slf4j
@Component(MSG_TYPE)
@RequiredArgsConstructor
public class HeartBeatMsgStrategy implements MsgStrategy {

    public static final String MSG_TYPE = "HEART_BEAT";

    /**
     * 处理消息
     *
     * @param message 消息对象
     * @param conn    当前连接
     */
    @Override
    public void handleMsg(Message message, Connection conn) {
        log.info("收到客户端心跳消息：sessionId={}", conn.getSession().getId());
        conn.send(message);
    }
}
