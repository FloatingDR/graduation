package com.cafuc.graduation.model.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 照片模板信息
 * </p>
 *
 * @author flyeat
 * @since 2020-05-29
 */
@Data
@TableName("model")
@ApiModel(value = "ModelPo对象", description = "照片模板信息")
public class ModelPo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "模板名称", example = "计算机学院学士服模板 1")
    private String modelName;

    @ApiModelProperty(value = "模板地址", example = "user/taylor/model/000.png")
    private String modelPath;

    @ApiModelProperty(value = "模板描述", example = "学士服模板")
    private String modelDes;

    @ApiModelProperty(value = "模版类型， 0：clothes(学士服) 1：background(背景图片) 3：other(其他)", example = "0")
    private Integer modelType;


}
