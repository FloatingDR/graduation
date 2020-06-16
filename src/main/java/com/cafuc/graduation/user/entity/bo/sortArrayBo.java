package com.cafuc.graduation.user.entity.bo;

import com.cafuc.graduation.user.entity.po.UserPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 合照排序对象
 * </p>
 *
 * @author flyeat
 * @since 2020-06-16
 */
@Data
@ApiModel(value = "合照排序对象", description = "合照排序对象")
public class sortArrayBo {

    @ApiModelProperty(value = "行索引", example = "1")
    private Integer index;

    @ApiModelProperty(value = "学生")
    private List<UserPo> rowStu;
}
