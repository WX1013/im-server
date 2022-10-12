package org.wx.im.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.wx.im.dao.ChatMapper;
import org.wx.im.entity.Chat;
import org.wx.im.service.ChatService;

/**
 * <p>
 * 业务实现类
 * 聊天室
 * </p>
 *
 * @author WangXin
 * @date 2022-10-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {
}
