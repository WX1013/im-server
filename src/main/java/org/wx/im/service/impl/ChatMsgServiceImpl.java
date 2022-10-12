package org.wx.im.service.impl;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.wx.im.config.exception.BizAssert;
import org.wx.im.dao.ChatMsgMapper;
import org.wx.im.entity.ChatMember;
import org.wx.im.entity.ChatMsg;
import org.wx.im.model.vo.MsgVO;
import org.wx.im.service.ChatMemberService;
import org.wx.im.service.ChatMsgService;
import org.wx.im.utils.StrPool;
import org.wx.im.websocket.entity.Message;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务实现类
 * 聊天消息表
 * </p>
 *
 * @author WangXin
 * @date 2022-10-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMsgServiceImpl extends ServiceImpl<ChatMsgMapper, ChatMsg> implements ChatMsgService {

    private final ChatMemberService chatMemberService;

    /**
     * 获取聊天消息列表
     *
     * @param from from
     * @param to   to
     * @param sort sort
     * @return List<MsgVO>
     * @author WangXin
     * @date 2022/10/11 20:20
     **/
    @Override
    public List<MsgVO> listMsg(String from, String to, Integer sort) {
        BizAssert.isTrue(!from.equals(to), "自己不能和自己聊天");
        ChatMember chatMember = chatMemberService.getChat(from, to);
        // 标记所有未读消息为已读
        chatMemberService.readAll(chatMember);
        return this.lambdaQuery().eq(ChatMsg::getChatId, chatMember.getChatId()).orderByDesc(ChatMsg::getSort).list().stream().map(m -> {
            MsgVO vo = new MsgVO();
            vo.setMid(m.getClientMsgId());
            vo.setMsgType(m.getMsgType());
            vo.setContent(m.getContent());
            vo.setSort(m.getSort());
            vo.setIsOwn(m.getUserId().equals(from));
            vo.setSendTime(DateUtil.format(m.getCreateTime(), StrPool.YYYY_MM_DD_HH_MM_SS));
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 处理消息
     *
     * @param message message
     * @return boolean
     * @author WangXin
     * @date 2022/10/11 20:39
     **/
    @Override
    public boolean saveMsg(Message message) {
        ChatMember chatMember = chatMemberService.getChat(message.getFrom().getUid(), message.getTo());
        Long chatId = chatMember.getChatId();
        // 客户端msgId幂等校验
        ChatMsg chatMsg = this.lambdaQuery().eq(ChatMsg::getChatId, chatId).eq(ChatMsg::getClientMsgId, message.getMid()).one();
        if (chatMsg != null) {
            return false;
        }

        // 保存消息
        chatMsg = new ChatMsg();
        chatMsg.setChatId(chatId);
        chatMsg.setUserId(message.getFrom().getUid());
        chatMsg.setClientMsgId(message.getMid());
        chatMsg.setMsgType(message.getMsgType());
        ChatMsg maxSort = this.lambdaQuery().eq(ChatMsg::getChatId, chatId).orderByDesc(ChatMsg::getSort).one();
        chatMsg.setSort(maxSort == null ? 1 : (maxSort.getSort() + 1));
        chatMsg.setContent(message.getContent());
        if (!this.save(chatMsg)) {
            return false;
        }

        // 更新未读数
        chatMember.setUnread(chatMember.getUnread() + 1);
        return chatMemberService.updateById(chatMember);
    }
}
