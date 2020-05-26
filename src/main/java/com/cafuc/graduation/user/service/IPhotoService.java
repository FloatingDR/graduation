package com.cafuc.graduation.user.service;

import com.cafuc.graduation.user.entity.po.UserPo;

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
}
