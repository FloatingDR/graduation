package com.cafuc.graduation.config;

import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * <p>
 * 百度 AI 配置类
 * </p>
 *
 * @author shijintao@supconit.com
 * @date 2020/5/25 11:13
 */
@Configuration
public class BaiduAIConfig {

    @Value("${baidu.APP_ID}")
    private String app_id;

    @Value("${baidu.API_KEY}")
    private String api_key;

    @Value("${baidu.SECRET_KEY}")
    private String secret_key;

    private static String APP_ID;
    private static String API_KEY;
    private static String SECRET_KEY;


    @PostConstruct
    void init() {
        init(app_id, api_key, secret_key);
    }

    public static AipBodyAnalysis aipBodyAnalysis() {
        // 初始化一个AipBodyAnalysis
        AipBodyAnalysis client = new AipBodyAnalysis(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(100000);

        return client;
    }

    public static void init(String app_id, String api_key, String secret_key) {
        APP_ID = app_id;
        API_KEY = api_key;
        SECRET_KEY = secret_key;
    }

}
