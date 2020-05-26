package com.cafuc.graduation.user.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 系统登录表
 * </p>
 *
 * @author flyeat
 * @since 2020-05-26
 */
@Data
@TableName("login")
@ApiModel(value = "LoginPo对象", description = "系统登录表")
public class LoginPo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id", example = "1")
    private Long userId;

    @ApiModelProperty(value = "密码，默认 0000", example = "0000")
    private String password;

    @ApiModelProperty(value = "角色：student(普通学生),monitor（班长），teacher（辅导员），admin（系统管理员）", example = "student")
    private String role;

}
