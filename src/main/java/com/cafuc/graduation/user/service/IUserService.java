package com.cafuc.graduation.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cafuc.graduation.user.entity.bo.LoginBo;
import com.cafuc.graduation.user.entity.po.UserPo;

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
     * 用户登录
     * </p>
     *
     * @param loginBo loginBo
     * @return {@link UserPo }
     * @author shijintao@supconit.com
     * @date 2020/5/26 15:57
     */
    UserPo login(LoginBo loginBo);

    /**
     * <p>
     * 新增用户
     * </p>
     *
     * @param userPo userPo
     * @author shijintao@supconit.com
     * @date 2020/5/26 22:20
     */
    void saveUser(UserPo userPo);
}
