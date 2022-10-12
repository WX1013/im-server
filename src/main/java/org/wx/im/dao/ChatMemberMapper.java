package org.wx.im.dao;


import org.wx.im.entity.ChatMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * 聊天成员表
 * </p>
 *
 * @author WangXin
 * @date 2022-10-11
 */
@Repository
public interface ChatMemberMapper extends BaseMapper<ChatMember> {

}
