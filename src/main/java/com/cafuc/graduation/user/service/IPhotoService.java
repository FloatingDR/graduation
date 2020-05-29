package com.cafuc.graduation.user.service;

import com.cafuc.graduation.user.entity.po.UserPo;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.Future;

/**
 * <p>
 * 图像处理service接口
 * </p>
 *
 * @author flyeat
 * @date 2020/5/25 11:06
 */
public interface IPhotoService {

    /**
     * <p>
     * 人像前景抠图，透明背景
     * </p>
     *
     * @param user user
     * @author taylor
     * @date 2020/5/25 11:18
     */
    String foreground(UserPo user) throws Exception;

    /**
     * <p>
     * 百度AI异步抠图
     * </p>
     *
     * @return {@link Future <String> }
     * @author shijintao@supconit.com
     * @date 2020/5/25 13:41
     */
    Future<String> futureAnalyse(Long userId);

    /**
     * <p>
     * 上传图片
     * </p>
     *
     * @param userId 用户id
     * @param file   图片
     * @return {@link String }
     * @author shijintao@supconit.com
     * @date 2020/5/25 09:47
     */
    String upload(Long userId, MultipartFile file) throws Exception;


    /**
     * <p>
     * 单人合成学士服照片
     * </p>
     *
     * @param userPo     用户
     * @param modelId    模板id
     */
    String composite(UserPo userPo, Long modelId);
}

