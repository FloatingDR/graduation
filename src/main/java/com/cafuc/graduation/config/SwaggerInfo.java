package com.cafuc.graduation.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Swagger2 的配置信息
 * </p>
 *
 * @author taylor
 * @date 2020/2/17 17:01
 */
@Component
@ConfigurationProperties(prefix = "swagger")
@Data
public class SwaggerInfo {

    private String title;
    private String author;
    private String website;
    private String email;
    private String version;
    private String description;
    /**  扫描包路径 */
    private String package_url;

}
