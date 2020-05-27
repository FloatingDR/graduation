package com.cafuc.graduation.user.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cafuc.graduation.common.Constant;
import com.cafuc.graduation.response.HttpResult;
import com.cafuc.graduation.user.entity.po.InterfaceConfinePo;
import com.cafuc.graduation.user.entity.po.UserPo;
import com.cafuc.graduation.user.service.IInterfaceConfineService;
import com.cafuc.graduation.user.service.IPhotoService;
import com.cafuc.graduation.user.service.IUserService;
import com.cafuc.graduation.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
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
@RestController
@RequestMapping("/photo")
@Api(tags = "照片接口")
public class PhotoController {

    @Value("${interfaceLimit.photoAnalyse.interfacePath}")
    private String photoAnalyseInterfacePath;

    private final IUserService userService;
    private final IPhotoService photoService;
    private final IInterfaceConfineService interfaceConfineService;

    @Autowired
    public PhotoController(IUserService userService, IPhotoService photoService, IInterfaceConfineService interfaceConfineService) {
        this.userService = userService;
        this.photoService = photoService;
        this.interfaceConfineService = interfaceConfineService;
    }

    @PostMapping("/upload/{id}")
    @ApiOperation(value = "上传照片", notes = "上传照片")
    public HttpResult<Boolean> upload(@PathVariable @ApiParam("用户id") Long id,
                                      @RequestParam("file") @ApiParam("照片") MultipartFile file) throws Exception {
        // 先检测是还可以调用该接口
        if (interfaceConfineService.getByUserId(id) != null) {
            String invokingAble = interfaceConfineService.queryInvokingAble(id, photoAnalyseInterfacePath);
            String[] invokingArr = invokingAble.split("_");
            int residue = Integer.parseInt(invokingArr[0]);
            String limitInfo = invokingArr[1];
            if (residue <= 0 && limitInfo.equals("limited")) {
                return HttpResult.error("您的上传次数已用完");
            }
        }

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
        if (userPo == null) {
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }
        // 下载上传的原图
        return FileUtil.httpDownloadFile(userPo.getPhoto());
    }


    @GetMapping("/download/analysedPhoto/{userId}")
    @ApiOperation(value = "下载抠图后的图片", notes = "下载抠图后的图片")
    public ResponseEntity<byte[]> downloadAnalysedPhoto(@PathVariable @ApiParam("用户id") Long userId) {
        UserPo userPo = userService.getById(userId);
        if (userPo == null) {
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }
        // 下载之前验证一下抠图图片是否成功生成
        if (!userPo.getAnalysedState().equals(Constant.ANALYSED_SUCCESS)) {
            log.info("用户-{}获取抠图图片未上传成功", userId);
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }
        // 下载抠图图片
        return FileUtil.httpDownloadFile(userPo.getAnalysedPhoto());
    }

    /**
     * <p>
     * 图片处理结果 MAP，用于构造消息返回提醒
     * </p>
     */
    private Map<Integer, String> MESSAGE_MAP = new HashMap<Integer, String>() {{
        put(Constant.ANALYSED_UNDO, "您的图片还未上传");
        put(Constant.ANALYSED_ING, "您的图片正在处理，请稍后");
        put(Constant.ANALYSED_FAIL, "图片处理失败，请重写上传");
        put(Constant.ANALYSED_SUCCESS, "图片处理完成");
    }};

    @GetMapping("/state/analysedPhoto/{userId}")
    @ApiOperation(value = "获取用户头像的AI处理结果", notes = "获取用户头像的AI处理结果")
    public HttpResult<Integer> getAnalysedPhotoState(@PathVariable @ApiParam("用户id") Long userId) {
        UserPo userPo = userService.getById(userId);
        if (userPo == null) {
            return HttpResult.error("该用户不存在");
        }
        Integer state = userPo.getAnalysedState();
        String message = MESSAGE_MAP.get(state);
        return HttpResult.success(state, message);
    }

}
