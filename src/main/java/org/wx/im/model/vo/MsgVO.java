package org.wx.im.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author WangXin
 * @description
 * @date 2022/10/11 20:17
 * @classname MsgVO
 */
@ApiModel("聊天消息")
@Data
public class MsgVO {

    @ApiModelProperty("客户端传入的消息id")
    private String mid;

    @ApiModelProperty("消息类型")
    private String msgType;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("消息序号")
    private Integer sort;

    @ApiModelProperty("是否是自己发的消息")
    private Boolean isOwn;

    @ApiModelProperty("发送时间")
    private String sendTime;

}
