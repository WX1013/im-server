package org.wx.im.config.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author WangXin
 * @description SwaggerConfig
 * @date 2022/9/8 14:00
 * @classname SwaggerConfig
 */
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {

    @Bean
    public Docket moduleDocket() {
        return  new Docket(DocumentationType.SWAGGER_2)
                .groupName("在线聊天在线接口文档")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.wx.im.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("在线聊天在线接口文档")
                .description("在线聊天在线接口文档")
                .license("Powered By WangXin")
                .licenseUrl("http://127.0.0.1")
                .termsOfServiceUrl("http://127.0.0.1")
                .contact(new Contact("Wangxin", "iamwx.cn", "iamwx@foxmail.com"))
                .version("v1.0.0")
                .build();
    }

}
