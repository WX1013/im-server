package org.wx.im.strategy.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.wx.im.config.exception.BizAssert;
import org.wx.im.dao.ChatMsgMapper;
import org.wx.im.entity.ChatMsg;
import org.wx.im.service.ChatMemberService;
import org.wx.im.strategy.MsgStrategy;
import org.wx.im.utils.SqlConstant;
import org.wx.im.websocket.connection.Connection;
import org.wx.im.websocket.entity.Message;

import static org.wx.im.strategy.impl.ReadMsgStrategy.MSG_TYPE;


/**
 * @author WangXin
 * @description 置消息为已读
 * @date 2022/3/25 17:24
 */
@Slf4j
@Component(MSG_TYPE)
@RequiredArgsConstructor
public class ReadMsgStrategy implements MsgStrategy {

    public static final String MSG_TYPE = "READ_MESSAGE";

    private final ChatMsgMapper chatMsgMapper;
    private final ChatMemberService chatMemberService;

    /**
     * 处理消息
     *
     * @param message 消息对象
     * @param conn    当前连接
     */
    @Override
    public void handleMsg(Message message, Connection conn) {
        ChatMsg msg = chatMsgMapper.selectOne(new QueryWrapper<ChatMsg>()
                .eq(ChatMsg.CLIENT_MSG_ID, message.getContent()).last(SqlConstant.LIMIT_1));
        BizAssert.notNull(msg, "消息不存在");

        // 更新自己的未读数
        chatMemberService.readOne(msg.getChatId(), msg.getId(), message.getFrom().getUid());
    }
}
