package com.cafuc.graduation.user.service.impl;

import com.cafuc.graduation.user.entity.po.LoginPo;
import com.cafuc.graduation.user.dao.LoginDao;
import com.cafuc.graduation.user.service.ILoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统登录表 服务实现类
 * </p>
 *
 * @author flyeat
 * @since 2020-05-26
 */
@Service
public class LoginServiceImpl extends ServiceImpl<LoginDao, LoginPo> implements ILoginService {

}
