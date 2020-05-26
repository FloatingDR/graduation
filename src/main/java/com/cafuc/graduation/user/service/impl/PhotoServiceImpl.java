package com.cafuc.graduation.user.service.impl;

import com.cafuc.graduation.common.Constant;
import com.cafuc.graduation.config.BaiduAIConfig;
import com.cafuc.graduation.exception.BaseException;
import com.cafuc.graduation.user.entity.po.UserPo;
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
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
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

    private final IUserService userService;

    @Autowired
    public PhotoServiceImpl(IUserService userService) {
        this.userService = userService;
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
        return FileUtil.ByteToFile(bytes, photo);
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
        });
        // 保存图片位置及状态
        userService.updateById(userPo);
        log.info("id为 {} 的头像 AI 抠图工作已完成", userId);
        return new AsyncResult<>(analysedUrl);
    }

    @Override
    public String upload(Long userId, MultipartFile file) throws Exception {
        String fileName = getFileName(userId, file);
        Path p = Paths.get(path + fileName);
        Path newFile = null;
        if (!Files.exists(p)) {
            newFile = Files.createFile(p);
        }
        InputStream in = file.getInputStream();
        assert newFile != null;
        Files.copy(in, newFile, StandardCopyOption.REPLACE_EXISTING);
        return path + fileName;
    }


    @Override
    public void composite() throws Exception {
//        BufferedImage person = Thumbnails.of("C:/Users/84612/Desktop/graduation/src/main/resources/photo/person_analysed.png").size(230,310).asBufferedImage();
//        Thumbnails.of("C:/Users/84612/Desktop/graduation/src/main/resources/photo/model.png")
//                .size(588,1290)
//                .watermark(new Coordinate(185,27), person, 1.0f)
//                .toFile("C:/Users/84612/Desktop/graduation/src/main/resources/photo/composite_result.png");
        BufferedImage person = Thumbnails.of("C:/Users/84612/Desktop/graduation/src/main/resources/photo/person2_analysed.png").size(230, 310).asBufferedImage();
        BufferedImage model = Thumbnails.of("C:/Users/84612/Desktop/graduation/src/main/resources/photo/model.png").size(588, 1290).asBufferedImage();
        Thumbnails.of("C:/Users/84612/Desktop/graduation/src/main/resources/photo/model.png")   //底图
                .size(588, 1290)     //必需，底图大小
                .watermark(new Coordinate(185, 27), person, 1.0f)    //水印1 watermark(position，image，opacity)
                .watermark(Positions.CENTER, model, 1.0f)     //水印2
                .toFile("C:/Users/84612/Desktop/graduation/src/main/resources/photo/composite_result.png");     //输出图片
    }


    //------------------------------------------------------------------
    //        utils
    //------------------------------------------------------------------


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
    private String getFileName(Long userId, MultipartFile file) throws Exception {
        // 检查文件
        checkImgValid(file);
        // 检查权限
        Path dir = Paths.get(path);

        if (!Files.exists(dir)) {
            Files.createDirectory(dir);
        }
        String fileType = getFileType(file);
        UserPo user = userService.getById(userId);
        return uuid() + "_" + user.getUserNum() + "." + fileType;
    }

    private String uuid() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    private void checkImgValid(MultipartFile originFile) throws Exception {
        //校验文件格式
        if (originFile == null) {
            throw new BaseException("未检测到文件");
        }
        if (originFile.getOriginalFilename() == null) {
            throw new BaseException("文件名不能为空");
        }
        String suffixs = ".bmp.jpg.png.BMP.JPG.PNG.jpeg.JPEG";
        String suffix = originFile.getOriginalFilename().substring(originFile.getOriginalFilename()
                .lastIndexOf("."));
        if (!suffixs.contains(suffix)) {
            throw new BaseException("图片格式有误，必须为bmp, jpg, png，jpeg图片格式中的一种");
        }
    }

    private String getFileType(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        int i = originalFilename.lastIndexOf(".");
        return originalFilename.substring(i + 1);
    }


}
