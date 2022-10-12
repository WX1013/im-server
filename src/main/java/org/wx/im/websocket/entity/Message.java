package org.wx.im.websocket.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author WangXin
 * @description 消息对象
 * @date 2022/10/9 23:08
 * @classname Message
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("Websocket 消息对象")
public class Message {

    @ApiModelProperty("消息类型")
    private String msgType;

    @ApiModelProperty("发送者")
    private FromJsonVO from;

    @ApiModelProperty("前端生成的消息id，使用UUID即可")
    private String mid;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("接收者")
    private String to;

}
