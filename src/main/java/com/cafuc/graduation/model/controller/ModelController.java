package com.cafuc.graduation.model.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cafuc.graduation.model.entity.bo.SaveModelBo;
import com.cafuc.graduation.model.entity.dto.ModelDto;
import com.cafuc.graduation.model.entity.po.ModelPo;
import com.cafuc.graduation.model.entity.qo.ModelQo;
import com.cafuc.graduation.model.service.IModelService;
import com.cafuc.graduation.response.HttpResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 照片模板信息 前端控制器
 * </p>
 *
 * @author flyeat
 * @since 2020-05-29
 */
@RestController
@RequestMapping("/model")
@Api(tags = "模板信息接口")
public class ModelController {

    private final IModelService modelService;

    @Autowired
    public ModelController(IModelService modelService) {
        this.modelService = modelService;
    }

    @PostMapping("/upload/{modelName}/{modelDes}/{modelType}")
    @ApiOperation(value = "上传模版照片", notes = "上传模版照片")
    public HttpResult<Boolean> upload(@PathVariable @ApiParam(value = "模板名称", example = "计算机学院学士服模板 1")
                                              String modelName,
                                      @PathVariable @ApiParam(value = "模板描述", example = "学士服模板")
                                              String modelDes,
                                      @PathVariable @ApiParam(value = "模版类型， 0：clothes(学士服) 1：background(背景图片) 3：other(其他)", example = "0")
                                              Integer modelType,
                                      @RequestPart(name = "file") @ApiParam("模板图片")
                                              MultipartFile modelPhoto) {
        SaveModelBo modelBo = new SaveModelBo();
        modelBo.setModelName(modelName);
        modelBo.setModelDes(modelDes);
        modelBo.setModelType(modelType);
        Boolean save = modelService.upload(modelBo, modelPhoto);
        return save ? HttpResult.success(true, "上传成功") :
                HttpResult.error("上传失败");
    }

    @GetMapping("/listByCondition")
    @ApiOperation(value = "根据条件获取模板信息", notes = "根据条件获取模板信息")
    public HttpResult<List<ModelDto>> upload(@RequestBody @ApiParam("模版查询条件") ModelQo modelQo) {
        ModelPo modelPo = new ModelPo();
        BeanUtils.copyProperties(modelQo, modelPo);
        List<ModelPo> poList = modelService.list(new QueryWrapper<>(modelPo));

        // po 转为 dto
        List<ModelDto> result = poList.stream().map(po -> {
            ModelDto dto = new ModelDto();
            BeanUtils.copyProperties(po, dto);
            return dto;
        }).collect(Collectors.toList());

        return HttpResult.success(result);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除模板", notes = "删除模板")
    public HttpResult<Boolean> delete(@PathVariable @ApiParam("id") Long id) {
        return modelService.removeById(id) ?
                HttpResult.success(true, "删除成功") :
                HttpResult.error("删除失败");
    }

}
