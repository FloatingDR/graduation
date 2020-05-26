package com.cafuc.graduation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Swagger 配置类
 * </p>
 *
 * @author taylor
 * @date 2020/2/17 16:18
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final SwaggerInfo swaggerInfo;

    @Autowired
    public SwaggerConfig(SwaggerInfo swaggerInfo) {
        this.swaggerInfo = swaggerInfo;
    }

    @Bean
    public Docket createRestApi() {
        ParameterBuilder authorization = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        authorization.name("Authorization")
                .description("用户认证authorization,传入userId")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        pars.add(authorization.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerInfo.getPackage_url()))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    /**
     * 构建 api文档的详细信息函数,这里的注解引用的是哪个
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 页面标题
                .title(swaggerInfo.getTitle())
                // 创建人
                .contact(new Contact(swaggerInfo.getAuthor(), swaggerInfo.getWebsite(), swaggerInfo.getEmail()))
                // 版本号
                .version(swaggerInfo.getVersion())
                // 描述
                .description(swaggerInfo.getDescription())
                .build();
    }

}
