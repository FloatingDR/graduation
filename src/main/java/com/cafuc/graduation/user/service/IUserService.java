package com.cafuc.graduation.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cafuc.graduation.user.entity.po.UserPo;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.Future;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author flyeat
 * @since 2020-05-25
 */
public interface IUserService extends IService<UserPo> {

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
     * 百度AI异步抠图
     * </p>
     *
     * @return {@link Future<String> }
     * @author shijintao@supconit.com
     * @date 2020/5/25 13:41
     */
    Future<String> futureAnalyse(Long userId);

    void composite() throws Exception;
}
