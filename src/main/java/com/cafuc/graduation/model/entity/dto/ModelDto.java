package com.cafuc.graduation.model.entity.dto;

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
@ApiModel(value = "ModelDto对象", description = "ModelDto对象")
public class ModelDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id", example = "1")
    private Long id;

    @ApiModelProperty(value = "模板名称", example = "计算机学院学士服模板 1")
    private String modelName;

    @ApiModelProperty(value = "模板描述", example = "学士服模板")
    private String modelDes;

    @ApiModelProperty(value = "模版类型， 0：clothes(学士服) 1：background(背景图片) 3：other(其他)", example = "0")
    private Integer modelType;


}
