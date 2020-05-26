package com.cafuc.graduation.user.controller;


import com.cafuc.graduation.common.Constant;
import com.cafuc.graduation.response.HttpResult;
import com.cafuc.graduation.user.entity.dto.UserDto;
import com.cafuc.graduation.user.entity.po.UserPo;
import com.cafuc.graduation.user.service.IPhotoService;
import com.cafuc.graduation.user.service.IUserService;
import com.cafuc.graduation.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 * 照片 前端控制器
 * </p>
 *
 * @author flyeat
 * @date 2020/5/26 13:30
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/photo")
@Api(tags = "照片")
public class PhotoController {

    private final IUserService userService;
    private final IPhotoService photoService;

    @Autowired
    public PhotoController(IUserService userService, IPhotoService photoService) {
        this.userService = userService;
        this.photoService = photoService;
    }

    @PostMapping("/upload/{id}")
    @ApiOperation(value = "上传照片", notes = "上传照片")
    public HttpResult<Boolean> upload(@PathVariable @ApiParam("用户id") Long id,
                                      @RequestParam("file") @ApiParam("照片") MultipartFile file) throws Exception {
        String path;
        path = photoService.upload(id, file);

        UserPo userPo = new UserPo();
        userPo.setId(id);
        userPo.setPhoto(path);
        userPo.setAnalysedState(Constant.ANALYSED_UNDO);
        boolean update = userService.updateById(userPo);
        try {
            // 异步抠图
            Future<String> future = photoService.futureAnalyse(id);
            log.info("调度爬异步抠图线程为：{}", Thread.currentThread().getName());

            // 等待3s，如果没有完成异步返回
            String analysedPhoto = future.get(3, TimeUnit.SECONDS);
            userPo.setAnalysedPhoto(analysedPhoto);
        } catch (TimeoutException e) {
            log.info("AI 抠图超时，返回结果，后台正在执行...");
        }
        return update ? HttpResult.success(true, "上传成功") :
                HttpResult.error("上传失败");

    }

    @GetMapping("/download/originalPhoto/{userId}")
    @ApiOperation(value = "下载上传的原图", notes = "下载上传的原图")
    public ResponseEntity<byte[]> downloadOriginalPhoto(@PathVariable @ApiParam("用户id") Long userId) {
        UserPo userPo = userService.getById(userId);
        // 下载上传的原图
        return FileUtil.httpDownloadFile(userPo.getPhoto());
    }


    @GetMapping("/download/analysedPhoto/{userId}")
    @ApiOperation(value = "下载抠图后的图片", notes = "下载抠图后的图片")
    public ResponseEntity<byte[]> downloadAnalysedPhoto(@PathVariable @ApiParam("用户id") Long userId) {
        UserPo userPo = userService.getById(userId);
        // 下载之前验证一下抠图图片是否成功生成
        if (!userPo.getAnalysedState().equals(Constant.ANALYSED_SUCCESS)) {
            log.info("用户-{}获取抠图图片未上传成功", userId);
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }
        // 下载抠图图片
        return FileUtil.httpDownloadFile(userPo.getAnalysedPhoto());
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