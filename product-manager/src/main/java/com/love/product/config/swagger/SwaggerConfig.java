package com.love.product.config.swagger;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;


/**
 * @author
 * @date 2022-10-11 12:11
 * @describe Swagger开发文档
 */
@Slf4j
@Configuration
@EnableSwagger2WebMvc
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig implements ApplicationListener<WebServerInitializedEvent> {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.love"))
                .paths(PathSelectors.any())
                .build();

    }

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder().version("1.0.0")
                .title("Spring Cloud Swagger2 文档")
                .description("Spring Cloud Swagger2 文档")
                .termsOfServiceUrl("https://blog.csdn.net/lucky_fang?type=blog")
                .build();
    }

    private int serverPort;

    public int getPort() {
        return this.serverPort;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
        log.info("项目启动成功！接口文档地址: http://localhost:"+serverPort+"/doc.html");
    }
}
