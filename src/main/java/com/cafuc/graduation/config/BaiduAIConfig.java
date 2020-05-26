package com.cafuc.graduation.config;

import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    private String APP_ID;

    @Value("${baidu.API_KEY}")
    private String API_KEY;

    @Value("${baidu.SECRET_KEY}")
    private String SECRET_KEY;


    @Bean
    public AipBodyAnalysis aipBodyAnalysis() {
        // 初始化一个AipBodyAnalysis
        AipBodyAnalysis client = new AipBodyAnalysis(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(100000);

        return client;
    }
}
