package com.cafuc.graduation.user.controller;

import com.cafuc.graduation.config.BaiduAIConfig;
import com.cafuc.graduation.response.HttpResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 百度AI
 * </p>
 *
 * @author shijintao@supconit.com
 * @date 2020/5/26 14:28
 */
@Slf4j
@RestController
@RequestMapping("/baidu_ai")
@Api(tags = "百度AI")
public class BaiduAIController {

    /**
     * <p>
     * 动态配置百度AI不用重启服务器
     * </p>
     *
     * @param appId     appId
     * @param appKey    appKey
     * @param secretKey secretKey
     * @return {@link HttpResult<Boolean> }
     * @author shijintao@supconit.com
     * @date 2020/5/26 14:34
     */
    @PutMapping("/dynamic_config/{appId}/{appKey}/{secretKey}")
    @ApiOperation(value = "动态配置百度AI", notes = "动态配置百度AI，不用重启服务器")
    public HttpResult<Boolean> dynamicConfig(@PathVariable @ApiParam("用户id") String appId,
                                             @PathVariable @ApiParam("用户id") String appKey,
                                             @PathVariable @ApiParam("用户id") String secretKey) {
        BaiduAIConfig.init(appId, appKey, secretKey);
        return HttpResult.success(true, "修改成功");
    }

}
