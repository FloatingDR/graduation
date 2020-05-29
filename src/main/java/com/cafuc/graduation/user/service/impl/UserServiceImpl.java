package com.cafuc.graduation.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cafuc.graduation.user.dao.UserDao;
import com.cafuc.graduation.user.entity.bo.LoginBo;
import com.cafuc.graduation.user.entity.po.LoginPo;
import com.cafuc.graduation.user.entity.po.UserPo;
import com.cafuc.graduation.user.service.ILoginService;
import com.cafuc.graduation.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final ILoginService loginService;

    @Autowired
    public UserServiceImpl(ILoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public UserPo login(LoginBo loginBo) {
        // 获得学号对应的学生
        UserPo condition = new UserPo();
        condition.setUserNum(loginBo.getUserNum());
        UserPo user = getOne(new QueryWrapper<>(condition));
        if (user == null) return null;

        // 获得登录信息
        Long userId = user.getId();
        LoginPo loginPo = new LoginPo();
        loginPo.setUserId(userId);
        LoginPo login = loginService.getOne(new QueryWrapper<>(loginPo));

        if (login == null) return null;
        return login.getPassword().equals(loginBo.getPassword()) ? user : null;
    }

    @Override
    public void saveUser(UserPo userPo) {
        this.baseMapper.saveUser(userPo);
    }
}
