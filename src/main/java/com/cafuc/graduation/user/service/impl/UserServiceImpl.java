package com.cafuc.graduation.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cafuc.graduation.common.Constant;
import com.cafuc.graduation.exception.BaseException;
import com.cafuc.graduation.user.dao.UserDao;
import com.cafuc.graduation.user.entity.po.UserPo;
import com.cafuc.graduation.user.service.IPhotoService;
import com.cafuc.graduation.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Coordinate;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author flyeat
 * @since 2020-05-25
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao, UserPo> implements IUserService {

    @Value("${photo.url}")
    private String path;

    private final IPhotoService photoService;

    @Autowired
    public UserServiceImpl(IPhotoService photoService) {
        this.photoService = photoService;
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

    @Async
    @Override
    public Future<String> futureAnalyse(Long userId) {
        UserPo user = getById(userId);

        UserPo userPo = new UserPo();
        userPo.setId(userId);
        // 抠图中
        userPo.setAnalysedState(Constant.ANALYSED_ING);
        String analysedUrl = null;
        try {
            analysedUrl = photoService.foreground(user);
        } catch (Exception e) {
            // 抠图失败
            userPo.setAnalysedState(Constant.ANALYSED_FAIL);
            log.info("id为 {} 的头像AI扣取失败，失败原因{}", userId, e.getMessage());
        }
        Optional.ofNullable(analysedUrl).ifPresent(url -> {
            // 抠图成功
            userPo.setAnalysedState(Constant.ANALYSED_SUCCESS);
            userPo.setAnalysedPhoto(url);
        });
        // 保存图片位置及状态
        updateById(userPo);
        log.info("id为 {} 的头像 AI 抠图已完成，图片已保存", userId);
        return new AsyncResult<>(analysedUrl);
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
        UserPo user = getById(userId);
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
}
