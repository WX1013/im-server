package org.wx.im.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;
import org.wx.im.config.wrapper.SuperEntity;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * <p>
 * 实体类
 * 已读聊天消息表
 * </p>
 *
 * @author WangXin
 * @since 2022-10-11
 */
@Data
@Builder
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("chat_msg_read")
@AllArgsConstructor
public class ChatMsgRead extends SuperEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 消息发送者id
     */
    @TableField(value = "user_id", condition = LIKE)
    private String userId;

    /**
     * 聊天室id
     */
    @TableField("chat_id")
    private Long chatId;

    /**
     * 消息id
     */
    @TableField("msg_id")
    private Long msgId;

    /**
     * 0-正常 1-删除
     */
    @TableField("deleted")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;


    public static final String USER_ID = "user_id";
    public static final String CHAT_ID = "chat_id";
    public static final String MSG_ID = "msg_id";
    public static final String DELETED = "deleted";


}
