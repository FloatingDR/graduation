package com.cafuc.graduation.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cafuc.graduation.user.dao.UserDao;
import com.cafuc.graduation.user.entity.bo.LoginBo;
import com.cafuc.graduation.user.entity.po.UserPo;
import com.cafuc.graduation.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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

    @Override
    public UserPo login(LoginBo loginBo) {
        UserPo userPo = new UserPo();
        BeanUtils.copyProperties(loginBo, userPo);
        return getOne(new QueryWrapper<>(userPo));
    }
}
