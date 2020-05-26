package com.cafuc.graduation.user.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cafuc.graduation.common.Constant;
import com.cafuc.graduation.response.HttpResult;
import com.cafuc.graduation.user.entity.bo.InsertBo;
import com.cafuc.graduation.user.entity.bo.UpdateBo;
import com.cafuc.graduation.user.entity.dto.UserDto;
import com.cafuc.graduation.user.entity.po.UserPo;
import com.cafuc.graduation.user.service.IPhotoService;
import com.cafuc.graduation.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    @PostMapping("/upload/{id}")
    @ApiOperation(value = "上传照片", notes = "上传照片")
    public HttpResult<UserDto> upload(@PathVariable @ApiParam("用户id") Long id,
                                      @RequestParam("file") @ApiParam("照片") MultipartFile file) throws Exception {
        String path;
        path = userService.upload(id, file);

        UserPo userPo = new UserPo();
        userPo.setId(id);
        userPo.setPhoto(path);
        userPo.setAnalysedState(Constant.ANALYSED_UNDO);
        boolean update = userService.updateById(userPo);
        try {
            // 异步抠图
            Future<String> future = userService.futureAnalyse(id);
            log.info("调度爬异步抠图线程为：{}", Thread.currentThread().getName());

            // 等待3s，如果没有完成异步返回
            String analysedPhoto = future.get(3, TimeUnit.SECONDS);
            userPo.setAnalysedPhoto(analysedPhoto);
        } catch (TimeoutException e) {
            log.info("AI 抠图超时，返回结果，后台正在执行...");
        }
        UserDto result = transPo2Dto(userPo);
        return update ? HttpResult.success(result) :
                HttpResult.error("上传失败");

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
