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
 * 聊天消息表
 * </p>
 *
 * @author WangXin
 * @since 2022-10-11
 */
@Data
@Builder
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("chat_msg")
@AllArgsConstructor
public class ChatMsg extends SuperEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 客户端传入消息id
     */
    @TableField(value = "client_msg_id", condition = LIKE)
    private String clientMsgId;

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
     * 消息类型
     */
    @TableField(value = "msg_type", condition = LIKE)
    private String msgType;

    /**
     * 消息内容
     */
    @TableField(value = "content", condition = LIKE)
    private String content;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 0-正常 1-删除
     */
    @TableField("deleted")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;


    public static final String CLIENT_MSG_ID = "client_msg_id";
    public static final String USER_ID = "user_id";
    public static final String CHAT_ID = "chat_id";
    public static final String MSG_TYPE = "msg_type";
    public static final String CONTENT = "content";
    public static final String SORT = "sort";
    public static final String DELETED = "deleted";


}
