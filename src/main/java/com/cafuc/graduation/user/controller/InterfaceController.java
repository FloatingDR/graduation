package com.cafuc.graduation.user.controller;


import com.cafuc.graduation.response.HttpResult;
import com.cafuc.graduation.user.service.IInterfaceConfineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 接口管理 前端控制器
 * </p>
 *
 * @author flyeat
 * @date 2020/5/26 13:30
 */
@Slf4j
@RestController
@RequestMapping("/interfaceManage")
@Api(tags = "接口管理")
public class InterfaceController {

    @Value("${interfaceLimit.photoAnalyse.interfacePath}")
    private String photoAnalyseInterfacePath;

    private final IInterfaceConfineService interfaceConfineService;

    @Autowired
    public InterfaceController(IInterfaceConfineService interfaceConfineService) {
        this.interfaceConfineService = interfaceConfineService;
    }


    @GetMapping("/queryUploadPhotoInterfaceInfo/{userId}")
    @ApiOperation(value = "查询用户上传照片接口调用或使用情况", notes = "查询用户上传照片接口调用或使用情况")
    public HttpResult<Boolean> queryUploadPhotoInterfaceInfo(@PathVariable @ApiParam("用户id") Long userId) {
        // 先检测是还可以调用该接口
        String invokingAble = interfaceConfineService.queryInvokingAble(userId, photoAnalyseInterfacePath);
        String[] invokingArr = invokingAble.split("_");
        int residue = Integer.parseInt(invokingArr[0]);
        String limitInfo = invokingArr[1];
        if (residue <= 0 && limitInfo.equals("limited")) {
            return HttpResult.error("您的上传次数已用完,请明天再试！");
        } else if (residue <= 0 && limitInfo.equals("unlimited")) {
            return HttpResult.success(true, "您的上传次数还剩余%s次,次数用完仍可上传，不受限制", residue);
        } else {
            return HttpResult.success(true, "您的上传次数还剩余%s次,次数用完当天不可再上传", residue);
        }
    }


}
