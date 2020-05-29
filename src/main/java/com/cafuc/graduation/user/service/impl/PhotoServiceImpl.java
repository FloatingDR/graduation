package com.cafuc.graduation.user.service.impl;

import com.cafuc.graduation.common.Constant;
import com.cafuc.graduation.config.BaiduAIConfig;
import com.cafuc.graduation.model.entity.po.ModelPo;
import com.cafuc.graduation.model.service.IModelService;
import com.cafuc.graduation.user.entity.bo.InterfaceConfineBo;
import com.cafuc.graduation.user.entity.po.UserPo;
import com.cafuc.graduation.user.service.IInterfaceConfineService;
import com.cafuc.graduation.user.service.IPhotoService;
import com.cafuc.graduation.user.service.IUserService;
import com.cafuc.graduation.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Coordinate;
import net.coobird.thumbnailator.geometry.Positions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * <p>
 * 图像处理service实现类
 * </p>
 *
 * @author flyeat
 * @date 2020/5/25 11:07
 */
@Service
@Slf4j
public class PhotoServiceImpl implements IPhotoService {

    @Value("${photo.url}")
    private String path;

    @Value("${photo.analysed_suffix}")
    private String analysedSuffix;

    @Value("${photo.clothes_suffix}")
    private String clothesSuffix;

    @Value("${interfaceLimit.photoAnalyse.interfaceName}")
    private String photoAnalyseInterfaceName;

    @Value("${interfaceLimit.photoAnalyse.interfacePath}")
    private String photoAnalyseInterfacePath;

    @Value("${interfaceLimit.photoAnalyse.confineCount}")
    private Integer photoAnalyseConfineCount;

    private final IUserService userService;
    private final IInterfaceConfineService interfaceConfineService;
    private final IModelService modelService;

    @Autowired
    public PhotoServiceImpl(IUserService userService, IInterfaceConfineService interfaceConfineService,
                            IModelService modelService) {
        this.userService = userService;
        this.interfaceConfineService = interfaceConfineService;
        this.modelService = modelService;
    }

    @Override
    public String foreground(UserPo user) throws Exception {
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<>();
        options.put("type", "foreground");

        // 参数为本地路径
        String photo = user.getPhoto();

        // 参数为本地路径
        JSONObject res = BaiduAIConfig.aipBodyAnalysis().bodySeg(photo, options);
        String foreground = res.get("foreground").toString();
        byte[] bytes = Base64.getDecoder().decode(foreground);
        return FileUtil.ByteToFile(bytes, photo, analysedSuffix);
    }

    @Async
    @Override
    public Future<String> futureAnalyse(Long userId) {
        UserPo user = userService.getById(userId);

        UserPo userPo = new UserPo();
        userPo.setId(userId);
        // 抠图中
        userPo.setAnalysedState(Constant.ANALYSED_ING);
        String analysedUrl = null;
        try {
            analysedUrl = foreground(user);
        } catch (Exception e) {
            // 抠图失败
            userPo.setAnalysedState(Constant.ANALYSED_FAIL);
            log.info("id为 {} 的头像AI扣取失败，失败原因{}", userId, e.getMessage());
            e.printStackTrace();
        }
        Optional.ofNullable(analysedUrl).ifPresent(url -> {
            // 抠图成功
            userPo.setAnalysedState(Constant.ANALYSED_SUCCESS);
            userPo.setAnalysedPhoto(url);
            // 学士服照片设置为空
            userPo.setClothesPath("");
        });
        // 保存图片位置及状态
        userService.updateById(userPo);
        log.info("id为 {} 的头像 AI 抠图工作已完成", userId);

        // 限制调用次数
        InterfaceConfineBo interfaceConfineBo = new InterfaceConfineBo(userId, photoAnalyseInterfaceName,
                photoAnalyseInterfacePath, photoAnalyseConfineCount, true);
        interfaceConfineService.addOrAutoIncrease(interfaceConfineBo);

        return new AsyncResult<>(analysedUrl);
    }

    @Override
    public String upload(Long userId, MultipartFile file) throws Exception {
        String fileName = getFileName(userId, path, file);
        return FileUtil.saveFile(fileName, path, file);
    }

    @Override
    public String composite(UserPo user, Long modelId) {
        String clothesPath = composite(user, modelId, clothesSuffix);
        if (StringUtils.isEmpty(clothesPath)) {
            return null;
        }
        UserPo userPo = new UserPo();
        userPo.setId(user.getId());
        userPo.setClothesPath(clothesPath);
        userService.updateById(userPo);
        return clothesPath;
    }


    //------------------------------------------------------------------
    //        utils
    //------------------------------------------------------------------

    /**
     * <p>
     * 合成学士服照片
     * </p>
     *
     * @param userPo     user
     * @param modelId    modelId
     * @param saveSuffix 学士服照片后缀
     * @return {@link String }
     * @author shijintao@supconit.com
     * @date 2020/5/29 10:27
     */
    public String composite(UserPo userPo, Long modelId, String saveSuffix) {
        Objects.requireNonNull(userPo, "用户信息不能为空");
        ModelPo modelPo = modelService.getById(modelId);
        String modelPath = modelPo.getModelPath();
        String analysedPhoto = userPo.getAnalysedPhoto();
        String savePath = analysedPhoto.replace(analysedSuffix, saveSuffix);

        try {
            BufferedImage person = Thumbnails.of(analysedPhoto).size(230, 310).asBufferedImage();
            BufferedImage model = Thumbnails.of(modelPath).size(588, 1290).asBufferedImage();
            Thumbnails.of(modelPath)   //底图
                    .size(588, 1290)     //必需，底图大小
                    .watermark(new Coordinate(185, 27), person, 1.0f)    //水印1 watermark(position，image，opacity)
                    .watermark(Positions.CENTER, model, 1.0f)     //水印2
                    .toFile(savePath);     //输出图片
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return savePath;
    }


    /**
     * <p>
     * 文件名格式:
     * uuid_userNum.fileType
     * 例如：
     * 00329a_20160511000.jpg
     * </p>
     *
     * @param userId userId
     * @param file   photo
     * @return {@link String }
     * @author shijintao@supconit.com
     * @date 2020/5/25 09:51
     */
    private String getFileName(Long userId, String pathUrl, MultipartFile file) throws Exception {
        // 检查文件
        FileUtil.checkImgValid(file);
        // 检查权限
        Path dir = Paths.get(pathUrl);

        if (!Files.exists(dir)) {
            Files.createDirectory(dir);
        }
        String fileType = FileUtil.getFileType(file);
        UserPo user = userService.getById(userId);
        return FileUtil.uuid() + "_" + user.getUserNum() + "." + fileType;
    }

}
