package org.wx.im.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.wx.im.dao.ChatMapper;
import org.wx.im.dao.ChatMemberMapper;
import org.wx.im.dao.ChatMsgMapper;
import org.wx.im.dao.ChatMsgReadMapper;
import org.wx.im.entity.Chat;
import org.wx.im.entity.ChatMember;
import org.wx.im.entity.ChatMsg;
import org.wx.im.entity.ChatMsgRead;
import org.wx.im.service.ChatMemberService;
import org.wx.im.utils.SqlConstant;

/**
 * <p>
 * 业务实现类
 * 聊天成员表
 * </p>
 *
 * @author WangXin
 * @date 2022-10-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMemberServiceImpl extends ServiceImpl<ChatMemberMapper, ChatMember> implements ChatMemberService {

    private final ChatMapper chatMapper;
    private final ChatMsgReadMapper chatMsgReadMapper;
    private final ChatMsgMapper chatMsgMapper;

    /**
     * 获取聊天室
     *
     * @param from from
     * @param to   to
     * @return Long
     * @author WangXin
     * @date 2022/10/11 20:21
     **/
    @Override
    public synchronized ChatMember getChat(String from, String to) {
        ChatMember chatMember = this.lambdaQuery()
                .eq(ChatMember::getUserId, from)
                .eq(ChatMember::getOtherUserId, to)
                .last(SqlConstant.LIMIT_1).one();
        if (chatMember != null) {
            return chatMember;
        }
        Chat chat = new Chat();
        chatMapper.insert(chat);

        chatMember = new ChatMember();
        chatMember.setChatId(chat.getId());
        chatMember.setUserId(from);
        chatMember.setOtherUserId(to);
        this.save(chatMember);

        ChatMember otherChatMember = new ChatMember();
        otherChatMember.setChatId(chat.getId());
        otherChatMember.setUserId(to);
        otherChatMember.setOtherUserId(from);
        this.save(otherChatMember);

        return chatMember;
    }

    /**
     * 已读消息
     *
     * @param chatId chatId
     * @param msgId  msgId
     * @param uid    uid
     * @author WangXin
     * @date 2022/10/11 23:35
     **/
    @Override
    public void readOne(Long chatId, Long msgId, String uid) {
        ChatMember chatMember = this.getOne(new LambdaQueryWrapper<ChatMember>()
                .eq(ChatMember::getChatId, chatId)
                .eq(ChatMember::getUserId, uid)
                .last(SqlConstant.LIMIT_1));
        if (chatMember == null) {
            log.warn("用户 {} 不在聊天室 {} 中", uid, chatId);
            return;
        }

        ChatMsgRead read = chatMsgReadMapper.selectOne(new QueryWrapper<ChatMsgRead>()
                .eq(ChatMsgRead.MSG_ID, msgId)
                .eq(ChatMsgRead.USER_ID, uid)
                .last(SqlConstant.LIMIT_1)
        );
        if (read != null) {
            return;
        }
        read = ChatMsgRead.builder()
                .chatId(chatId)
                .msgId(msgId)
                .userId(uid)
                .build();
        chatMsgReadMapper.insert(read);

        // 更新数据库
        chatMember.setUnread(chatMember.getUnread() - 1);
        this.updateById(chatMember);
    }

    /**
     * 已读所有消息
     *
     * @param chatMember chatMember
     * @author WangXin
     * @date 2022/10/11 23:45
     **/
    @Override
    public void readAll(ChatMember chatMember) {
        chatMember.setUnread(0L);
        this.updateById(chatMember);

        // 增加已读记录
        chatMsgMapper.selectList(new QueryWrapper<ChatMsg>()
                .eq(ChatMsg.CHAT_ID, chatMember.getChatId())
                .ne(ChatMsg.USER_ID, chatMember.getUserId())
                .notExists("select 1 from chat_msg_read " +
                        "where deleted = 0 and msg_id = chat_msg.id " +
                        "and chat_id = {0} and user_id = {1}", chatMember.getChatId(), chatMember.getUserId())
        ).forEach(m -> {
            // 更新消息已读数
            ChatMsgRead read = ChatMsgRead.builder()
                    .chatId(chatMember.getChatId())
                    .msgId(m.getId())
                    .userId(chatMember.getUserId())
                    .build();
            chatMsgReadMapper.insert(read);
        });

    }
}
