package com.cafuc.graduation.user.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cafuc.graduation.common.Constant;
import com.cafuc.graduation.response.HttpResult;
import com.cafuc.graduation.user.entity.bo.InsertBo;
import com.cafuc.graduation.user.entity.bo.LoginBo;
import com.cafuc.graduation.user.entity.bo.UpdateBo;
import com.cafuc.graduation.user.entity.dto.UserDto;
import com.cafuc.graduation.user.entity.po.LoginPo;
import com.cafuc.graduation.user.entity.po.UserPo;
import com.cafuc.graduation.user.service.ILoginService;
import com.cafuc.graduation.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author flyeat
 * @since 2020-05-25
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController {

    private final IUserService userService;
    private final ILoginService loginService;

    @Autowired
    public UserController(IUserService userService, ILoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    @PostMapping("/login")
    @ApiOperation("登录")
    public HttpResult<UserDto> login(@RequestBody @ApiParam("登录实体") LoginBo loginBo) {
        try {
            Objects.requireNonNull(loginBo.getUserNum(), "学号不能为空");
            Objects.requireNonNull(loginBo.getPassword(), "密码不能为空");
        } catch (NullPointerException e) {
            return HttpResult.error("登录失败，%s", e.getMessage());
        }
        UserPo one = userService.login(loginBo);

        if (one == null) {
            return HttpResult.error("登录失败，请重试");
        }
        return HttpResult.success(transPo2Dto(one), "登录成功");
    }

    @PostMapping("/filterByCondition")
    @ApiOperation("根据条件过滤查找")
    public HttpResult<List<UserPo>> getByCondition(@RequestBody @ApiParam("查询条件") UserPo userPo) {
        LambdaQueryWrapper<UserPo> queryWrapper = new LambdaQueryWrapper<>(userPo);
        // 按学号降序
        queryWrapper.orderByAsc(UserPo::getUserNum);

        List<UserPo> list = userService.list(queryWrapper);
        return list == null ? HttpResult.error("没有符合条件的信息") :
                HttpResult.success(list);
    }

    @PostMapping("/addUser")
    @ApiOperation("添加用户信息")
    @Transactional(rollbackFor = Exception.class)
    public HttpResult<UserPo> addUser(@RequestBody @ApiParam("新增用户信息实体") InsertBo insertBo) {
        try {
            Objects.requireNonNull(insertBo.getUserNum(), "学号不能为空");
            Objects.requireNonNull(insertBo.getUserName(), "姓名不能为空");
            Objects.requireNonNull(insertBo.getCollege(), "学院不能为空");
            Objects.requireNonNull(insertBo.getProfession(), "专业不能为空");
            Objects.requireNonNull(insertBo.getClassName(), "班级不能为空");
        } catch (NullPointerException e) {
            log.info("添加用户信息失败，失败原因[{}]，失败信息[{}]", e.getMessage(), insertBo);
            return HttpResult.error(e.getMessage());
        }
        UserPo userPo = new UserPo();
        BeanUtils.copyProperties(insertBo, userPo);
        userService.saveUser(userPo);

        // 保存登录信息
        Long userId = userPo.getId();
        LoginPo loginPo = new LoginPo();
        loginPo.setUserId(userId);
        BeanUtils.copyProperties(insertBo, loginPo);
        boolean save = loginService.save(loginPo);
        return save ? HttpResult.success(userPo, "上传成功") :
                HttpResult.error("上传失败，请重试");
    }

    @PutMapping("/updateUser/{userId}")
    @ApiOperation("修改信息")
    public HttpResult<UserPo> updateUser(@ApiParam("用户id") @PathVariable Long userId,
                                         @RequestBody @ApiParam("携带id用户信息") UpdateBo updateBo) {
        UserPo userPo = new UserPo();
        BeanUtils.copyProperties(updateBo, userPo);
        userPo.setId(userId);
        boolean update = userService.updateById(userPo);
        UserPo newUser = userService.getById(userId);
        return update && newUser != null ? HttpResult.success(newUser) :
                HttpResult.error("修改失败");
    }

    @DeleteMapping("/deleteUser/{id}")
    @ApiOperation("删除用户信息")
    @Transactional(rollbackFor = Exception.class)
    public HttpResult<Boolean> deleteUser(@PathVariable @ApiParam("用户id") Long id) {
        boolean removeUser = userService.removeById(id);
        LoginPo loginPo = new LoginPo();
        loginPo.setUserId(id);
        boolean removeLogin = loginService.remove(new QueryWrapper<>(loginPo));
        return removeUser && removeLogin ? HttpResult.success(true, "删除成功") :
                HttpResult.error("删除失败");
    }

    @GetMapping("/queryUserRole/{id}")
    @ApiOperation("查看用户角色")
    public HttpResult<String> queryUserRole(@PathVariable @ApiParam("用户id") Long id) {
        LoginPo loginPo = new LoginPo();
        loginPo.setUserId(id);
        LoginPo po = loginService.getOne(new QueryWrapper<>(loginPo));
        if (po == null) {
            return HttpResult.error("没有该用户的信息");
        }
        return HttpResult.success(po.getRole());
    }

    @PutMapping("/modifyUserRole/{id}/{role}")
    @ApiOperation("修改用户角色")
    public HttpResult<Boolean> modifyUserRole(@PathVariable @ApiParam("用户id") Long id,
                                              @PathVariable @ApiParam("用户id") String role) {
        LoginPo loginPo = new LoginPo();
        loginPo.setUserId(id);
        LoginPo po = loginService.getOne(new QueryWrapper<>(loginPo));
        Optional.ofNullable(po).ifPresent(p -> p.setRole(role));
        return loginService.updateById(po) ? HttpResult.success(true, "修改成功") :
                HttpResult.error("修改失败");
    }


    //------------------------------------------------------------------
    //        utils
    //------------------------------------------------------------------


    /**
     * <p>
     * 持久化类转为传输类
     * </p>
     *
     * @param po 持久化类
     * @return {@link UserDto }
     * @author shijintao@supconit.com
     * @date 2020/5/26 13:08
     */
    private UserDto transPo2Dto(UserPo po) {
        UserDto result = new UserDto();
        BeanUtils.copyProperties(po, result);
        if (!po.getAnalysedState().equals(Constant.ANALYSED_SUCCESS)) {
            result.setAnalysedPhoto(null);
        }
        return result;
    }

}
