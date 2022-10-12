package org.wx.im.strategy.impl;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.wx.im.service.ChatMsgService;
import org.wx.im.strategy.MsgStrategy;
import org.wx.im.websocket.connection.Connection;
import org.wx.im.websocket.connection.ConnectionManager;
import org.wx.im.websocket.connection.UserSession;
import org.wx.im.websocket.entity.Message;

import static org.wx.im.strategy.impl.TextMsgStrategy.MSG_TYPE;


/**
 * @author WangXin
 * @description 文本消息
 * @date 2022/3/25 17:24
 */
@Slf4j
@Component(MSG_TYPE)
@RequiredArgsConstructor
public class TextMsgStrategy implements MsgStrategy {

    public static final String MSG_TYPE = "TEXT";

    private final UserSession userSession;
    private final ChatMsgService chatMsgService;

    /**
     * 处理消息
     *
     * @param message 消息对象
     * @param conn    当前连接
     */
    @Override
    public void handleMsg(Message message, Connection conn) {
        if(message.getFrom() == null || StrUtil.isEmpty(message.getFrom().getUid())){
            log.warn("WS消息异常：发送者对象数据为空");
            return;
        }
        // 处理消息
        boolean result = chatMsgService.saveMsg(message);

        if(!result){
            return;
        }

        // 转发消息给接受者
        userSession.getSessionIds(message.getTo()).forEach(sid -> {
            Connection toConn = ConnectionManager.getConnection(sid);
            if (toConn == null) {
                // 移除sessionId的缓存
                log.info("清除用户 {} 的 sessionId：{}", message.getTo(), sid);
                userSession.removeSessionId(message.getTo(), sid);
            } else {
                toConn.send(message);
            }
        });
    }
}
