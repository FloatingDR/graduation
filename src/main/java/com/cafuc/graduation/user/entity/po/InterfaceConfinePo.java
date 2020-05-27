package com.cafuc.graduation.user.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户-接口限制，限制调用次数
 * </p>
 *
 * @author flyeat
 * @since 2020-05-27
 */
@Data
@TableName("user_interface_confine")
@ApiModel(value = "InterfaceConfinePo对象", description = "用户-接口限制，限制调用次数")
public class InterfaceConfinePo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户已调用次数")
    private Integer userInvokeCount;

    @ApiModelProperty(value = "接口描述")
    private String interfaceName;

    @ApiModelProperty(value = "接口地址")
    private String interfacePath;

    @ApiModelProperty(value = "限制调用次数")
    private Integer confineCount;

    @ApiModelProperty(value = "超出调用次数是否限制 0：不限制 1：限制")
    private Boolean invokingForbid;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
