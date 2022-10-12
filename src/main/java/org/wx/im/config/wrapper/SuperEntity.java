package org.wx.im.config.wrapper;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @description 数据库DO对象的共同父类
 * 未把逻辑删除字段加入是因为有的表不需要逻辑删除字段
 * @Author: WangXin
 * @Date: 2021/8/5 下午9:40
 **/
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SuperEntity {

    public static final String FIELD_ID = "id";
    public static final String CREATE_TIME = "create_time";
    public static final String CREATE_TIME_FILED = "createTime";
    public static final String CREATE_USER = "create_user";
    public static final String CREATE_USER_FILED = "createUser";

    public static final String UPDATE_TIME = "update_time";
    public static final String UPDATE_TIME_FILED = "updateTime";
    public static final String UPDATE_USER = "update_user";
    public static final String UPDATE_USER_FILED = "updateUser";

    private static final long serialVersionUID = -4603650115461757622L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id")
    protected Long id;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    @ApiModelProperty(value = "创建人ID")
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    protected Long createUser;

    @ApiModelProperty(value = "修改时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    protected LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人ID")
    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    protected Long updateUser;

}
