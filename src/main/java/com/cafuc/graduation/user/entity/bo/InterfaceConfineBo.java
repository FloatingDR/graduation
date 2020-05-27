package com.cafuc.graduation.user.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 用户-接口限制，限制调用次数
 * </p>
 *
 * @author flyeat
 * @since 2020-05-27
 */
@Data
@ApiModel(value = "InterfaceConfineBo对象", description = "用户-接口限制，限制调用次数")
@NoArgsConstructor
@AllArgsConstructor
public class InterfaceConfineBo{

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "接口描述")
    private String interfaceName;

    @ApiModelProperty(value = "接口地址")
    private String interfacePath;

    @ApiModelProperty(value = "限制调用次数")
    private Integer confineCount;

    @ApiModelProperty(value = "超出调用次数是否限制 0：不限制 1：限制")
    private Boolean invokingForbid;

}
