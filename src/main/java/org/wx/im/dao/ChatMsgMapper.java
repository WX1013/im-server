package org.wx.im.dao;


import org.wx.im.entity.ChatMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * 聊天消息表
 * </p>
 *
 * @author WangXin
 * @date 2022-10-11
 */
@Repository
public interface ChatMsgMapper extends BaseMapper<ChatMsg> {

}
