package com.cafuc.graduation.user.service.impl;

import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import com.cafuc.graduation.user.entity.po.UserPo;
import com.cafuc.graduation.user.service.IPhotoService;
import com.cafuc.graduation.util.FileUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;

/**
 * <p>
 * 图像处理service实现类
 * </p>
 *
 * @author flyeat
 * @date 2020/5/25 11:07
 */
@Service
public class PhotoServiceImpl implements IPhotoService {

    // 百度AI人体分析
    private final AipBodyAnalysis aipBodyAnalysis;

    @Autowired
    public PhotoServiceImpl(AipBodyAnalysis aipBodyAnalysis) {
        this.aipBodyAnalysis = aipBodyAnalysis;
    }


    @Override
    public String foreground(UserPo user) throws Exception {
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<>();
        options.put("type", "foreground");

        // 参数为本地路径
        String photo = user.getPhoto();

        // 参数为本地路径
        JSONObject res = aipBodyAnalysis.bodySeg(photo, options);
        String foreground = res.get("foreground").toString();
        byte[] bytes = Base64.getDecoder().decode(foreground);
        return FileUtil.ByteToFile(bytes, photo);
    }
}
