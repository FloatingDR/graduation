package com.cafuc.graduation.user.controller;

import com.cafuc.graduation.response.HttpResult;
import com.cafuc.graduation.user.entity.bo.sortArrayBo;
import com.cafuc.graduation.user.service.IInterfaceConfineService;
import com.cafuc.graduation.user.service.IPhotoService;
import com.cafuc.graduation.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 合照 接口
 * </p>
 *
 * @author flyeat
 * @date 2020/6/06 21:00
 */
@Slf4j
@RestController
@RequestMapping("/groupPhoto")
@Api(tags = "合照接口")
public class GroupPhotoController {

    private final IUserService userService;
    private final IPhotoService photoService;

    @Autowired
    public GroupPhotoController(IUserService userService, IPhotoService photoService, IInterfaceConfineService interfaceConfineService) {
        this.userService = userService;
        this.photoService = photoService;
    }

    @PostMapping("/compositeGroupPhoto/{userId}")
    @ApiOperation(value = "合成合照", notes = "合成合照")
    public HttpResult<Boolean> compositeGroupPhoto(@PathVariable Long userId,
                                                   @RequestBody String jsonString) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        String listString = jsonObject.getString("sortMethod");
        List<sortArrayBo> sortArray = JSONArray.parseArray(listString,sortArrayBo.class);
        System.out.println(userId+" "+sortArray);
        return HttpResult.success(true,"测试接口");
    }

}
