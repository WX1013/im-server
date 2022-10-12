package org.wx.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.wx.im.entity.ChatMsg;
import org.wx.im.model.vo.MsgVO;
import org.wx.im.websocket.entity.Message;

import java.util.List;

/**
 * <p>
 * 业务接口
 * 聊天消息表
 * </p>
 *
 * @author WangXin
 * @date 2022-10-11
 */
public interface ChatMsgService extends IService<ChatMsg> {

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
    List<MsgVO> listMsg(String from, String to, Integer sort);

    /**
     * 处理消息
     *
     * @param message message
     * @return boolean
     * @author WangXin
     * @date 2022/10/11 20:39
     **/
    boolean saveMsg(Message message);
}
