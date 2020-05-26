package com.cafuc.graduation.user.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cafuc.graduation.response.HttpResult;
import com.cafuc.graduation.user.entity.bo.InsertBo;
import com.cafuc.graduation.user.entity.bo.UpdateBo;
import com.cafuc.graduation.user.entity.po.UserPo;
import com.cafuc.graduation.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author flyeat
 * @since 2020-05-25
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/user")
@Api(tags = "用户")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/getByCondition")
    @ApiOperation("条件获取用户信息")
    public HttpResult<UserPo> getByCondition(@RequestBody @ApiParam("查询条件") UserPo userPo) {
        UserPo one = userService.getOne(new QueryWrapper<>(userPo));
        return one == null ? HttpResult.error("没有符合条件的信息") :
                HttpResult.success(one);
    }

    @PostMapping("/addUser")
    @ApiOperation("添加用户信息")
    public HttpResult<UserPo> addUser(@RequestBody @ApiParam("新增用户信息实体") InsertBo insertBo) {
        try {
            Objects.requireNonNull(insertBo.getUserName(), "学号不能为空");
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
        boolean save = userService.save(userPo);
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
    public HttpResult<Boolean> deleteUser(@PathVariable @ApiParam("用户id") Long id) {
        boolean remove = userService.removeById(id);
        return remove ? HttpResult.success(true, "删除成功") :
                HttpResult.error("删除失败");
    }

}
