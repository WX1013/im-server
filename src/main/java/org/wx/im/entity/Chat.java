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
 * 聊天室
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
@TableName("chat")
@AllArgsConstructor
public class Chat extends SuperEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 上一次消息id
     */
    @TableField("last_msg_id")
    private Long lastMsgId;

    /**
     * 上一次的消息类型
     */
    @TableField(value = "last_msg_type", condition = LIKE)
    private String lastMsgType;

    /**
     * 上一次的消息内容
     */
    @TableField(value = "last_msg_content", condition = LIKE)
    private String lastMsgContent;

    /**
     * 0-正常 1-删除
     */
    @TableField("deleted")
    @TableLogic(value = "0",delval = "1")
    private Integer deleted;


    public static final String LAST_MSG_ID = "last_msg_id";
    public static final String LAST_MSG_TYPE = "last_msg_type";
    public static final String LAST_MSG_CONTENT = "last_msg_content";
    public static final String DELETED = "deleted";


}
