package com.cafuc.graduation.model.service;

import com.cafuc.graduation.model.entity.bo.SaveModelBo;
import com.cafuc.graduation.model.entity.po.ModelPo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 照片模板信息 服务类
 * </p>
 *
 * @author flyeat
 * @since 2020-05-29
 */
public interface IModelService extends IService<ModelPo> {

    /**
     * <p>
     * 上传模版照片
     * </p>
     *
     * @param saveModelBo 上传模板信息
     * @param file        图片
     * @return {@link Boolean }
     * @author shijintao@supconit.com
     * @date 2020/5/29 09:27
     */
    Boolean upload(SaveModelBo saveModelBo, MultipartFile file);

}
