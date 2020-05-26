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
@ApiModel(value = "用户新增对象", description = "用户新增对象")
public class InsertBo implements Serializable {

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
