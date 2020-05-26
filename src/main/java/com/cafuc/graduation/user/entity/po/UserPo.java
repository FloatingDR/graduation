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
 * 用户表
 * </p>
 *
 * @author flyeat
 * @since 2020-05-25
 */
@Data
@TableName("user")
@ApiModel(value = "UserPo对象", description = "用户表")
public class UserPo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "学号", example = "20160511000")
    private String userNum;

    @ApiModelProperty(value = "密码，默认 0000 ", example = "0000")
    private String password;

    @ApiModelProperty(value = "姓名", example = "张三")
    private String userName;

    @ApiModelProperty(value = "学院", example = "计算机学院")
    private String college;

    @ApiModelProperty(value = "专业", example = "计算机科学与技术")
    private String profession;

    @ApiModelProperty(value = "班级", example = "1602")
    private String className;

    @ApiModelProperty(value = "图片地址", example = "D://user/photo/9df6897a52744983bdc8b844e4c2d7b9_20160511000.jpeg")
    private String photo;

    @ApiModelProperty(value = "抠图后的图片地址", example = "D://user/photo/9df6897a52744983bdc8b844e4c2d7b9_20160511000_analysed.jpeg")
    private String analysedPhoto;

    @ApiModelProperty(value = "抠图结果 0 - 未上传；1 - 上传中；2 - 上传失败；3 - 上传成功；", example = "0")
    private Integer analysedState;

}
