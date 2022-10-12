package org.wx.im.strategy;

import org.wx.im.websocket.connection.Connection;
import org.wx.im.websocket.entity.Message;

/**
 * @author WangXin
 * @description 消息策略器类
 * @date 2022/3/25 17:22
 */
public interface MsgStrategy {

    /**
     * 处理消息
     *
     * @param message 消息对象
     * @param conn    当前连接
     */
    void handleMsg(Message message, Connection conn);

}
