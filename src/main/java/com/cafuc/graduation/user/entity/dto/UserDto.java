package com.cafuc.graduation.user.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * userDto
 * </p>
 *
 * @author flyeat
 * @date 2020/5/26 13:00
 */
@Data
@ApiModel(value = "用户传输对象", description = "用户传输对象")
public class UserDto {

    @ApiModelProperty(value = "自增id", example = "1")
    private Long id;

    @ApiModelProperty(value = "学号", example = "20160511000")
    private String userNum;

    @ApiModelProperty(value = "姓名", example = "张三")
    private String userName;

    @ApiModelProperty(value = "学院", example = "计算机学院")
    private String college;

    @ApiModelProperty(value = "专业", example = "计算机科学与技术")
    private String profession;

    @ApiModelProperty(value = "班级", example = "1602")
    private String className;

}
