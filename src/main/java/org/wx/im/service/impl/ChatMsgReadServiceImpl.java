package org.wx.im.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.wx.im.dao.ChatMsgReadMapper;
import org.wx.im.entity.ChatMsgRead;
import org.wx.im.service.ChatMsgReadService;

/**
 * <p>
 * 业务实现类
 * 已读聊天消息表
 * </p>
 *
 * @author WangXin
 * @date 2022-10-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMsgReadServiceImpl extends ServiceImpl<ChatMsgReadMapper, ChatMsgRead> implements ChatMsgReadService {
}
