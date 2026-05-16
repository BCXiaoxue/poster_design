package com.govy.common.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author:Govy
 * @Date:2021/7/27
 * @Desc: Knife4j配置
 * */
@Configuration
@Slf4j
@EnableKnife4j // 启用 Knife4j 增强功能
public class Knife4jConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        log.info("开始创建knife4j接口文档...");
        return new OpenAPI().info(
                new Info()
                        .title("后台管理系统API")
                        .version("1.0")
                        .description("后台管理系统API"));
    }

//    @Bean
//    public GroupedOpenApi systemAPI() {
//
//        return GroupedOpenApi.builder().group("系统信息管理").
//                pathsToMatch(
//                        "/admin/system/**"
//                ).
//                build();
//    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("用户端接口")
                .packagesToScan("com.govy.base.controller.user") // 指定 Controller 扫描路径
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicApi3() {
        return GroupedOpenApi.builder()
                .group("管理端接口")
                .packagesToScan("com.govy.base.controller.admin") // 指定 Controller 扫描路径
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicApi2() {
        return GroupedOpenApi.builder()
                .group("通用模块接口")
                .packagesToScan("com.govy.base.controller.common") // 指定 Controller 扫描路径
                .pathsToMatch("/common/**")
                .build();
    }
}