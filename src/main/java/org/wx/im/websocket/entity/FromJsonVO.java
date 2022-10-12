package org.wx.im.websocket.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author WangXin
 * @description
 * @date 2022/10/11 19:03
 * @classname FromJsonVO
 */
@ApiModel("发送消息的用户")
@Data
public class FromJsonVO {

    @ApiModelProperty("用户id")
    private String uid;

    @ApiModelProperty("用户名称")
    private String name;

    @ApiModelProperty("用户头像")
    private String avatar;

}
