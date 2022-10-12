package org.wx.im.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.wx.im.entity.ChatMsgRead;

/**
 * <p>
 * Mapper 接口
 * 已读聊天消息表
 * </p>
 *
 * @author WangXin
 * @date 2022-10-11
 */
@Repository
public interface ChatMsgReadMapper extends BaseMapper<ChatMsgRead> {

}
