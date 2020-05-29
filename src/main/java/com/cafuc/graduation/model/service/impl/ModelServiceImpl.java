package com.cafuc.graduation.model.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cafuc.graduation.model.dao.ModelDao;
import com.cafuc.graduation.model.entity.bo.SaveModelBo;
import com.cafuc.graduation.model.entity.po.ModelPo;
import com.cafuc.graduation.model.service.IModelService;
import com.cafuc.graduation.util.FileUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <p>
 * 照片模板信息 服务实现类
 * </p>
 *
 * @author flyeat
 * @since 2020-05-29
 */
@Service
public class ModelServiceImpl extends ServiceImpl<ModelDao, ModelPo> implements IModelService {

    @Value("${model.url}")
    private String modelUrl;

    @Override
    public Boolean upload(SaveModelBo saveModelBo, MultipartFile file) {
        ModelPo po = new ModelPo();
        BeanUtils.copyProperties(saveModelBo, po);
        String savePath;
        try {
            String fileName = getFileName(file, modelUrl);
            savePath = FileUtil.saveFile(fileName, modelUrl, file);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        po.setModelPath(savePath);
        return save(po);
    }


    //------------------------------------------------------------------
    //        utils
    //------------------------------------------------------------------

    private String getFileName(MultipartFile file, String filePath) throws Exception {
        // 检查文件
        FileUtil.checkImgValid(file);
        Path dir = Paths.get(filePath);

        if (!Files.exists(dir)) {
            Files.createDirectory(dir);
        }
        String fileType = FileUtil.getFileType(file);
        return FileUtil.uuid() + "_model." + fileType;
    }

}
