package org.wx.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.wx.im.entity.ChatMember;

/**
 * <p>
 * 业务接口
 * 聊天成员表
 * </p>
 *
 * @author WangXin
 * @date 2022-10-11
 */
public interface ChatMemberService extends IService<ChatMember> {

    /**
     * 获取聊天室
     *
     * @param from from
     * @param to   to
     * @return Long
     * @author WangXin
     * @date 2022/10/11 20:21
     **/
    ChatMember getChat(String from, String to);

    /**
     * 已读消息
     *
     * @param chatId chatId
     * @param msgId  msgId
     * @param uid    uid
     * @author WangXin
     * @date 2022/10/11 23:35
     **/
    void readOne(Long chatId, Long msgId, String uid);

    /**
     * 已读所有消息
     *
     * @param chatMember chatMember
     * @author WangXin
     * @date 2022/10/11 23:45
     **/
    void readAll(ChatMember chatMember);

}
