package com.cafuc.graduation.user.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户新增对象
 * </p>
 *
 * @author flyeat
 * @since 2020-05-25
 */
@Data
@ApiModel(value = "登录实体", description = "登录实体")
public class LoginBo implements Serializable {

    @ApiModelProperty(value = "学号", example = "20160511000")
    private String userNum;

    @ApiModelProperty(value = "密码，默认 0000", example = "0000")
    private String password;

}
