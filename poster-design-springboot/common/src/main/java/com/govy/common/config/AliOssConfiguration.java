package com.govy.common.config;


import com.govy.common.properties.AliOssProperties;
import com.govy.common.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建AliOssUtil对象
 */
@Configuration
@Slf4j
@ConditionalOnProperty(name = "aliyun.oss.endpoint")   // 判断配置文件中是否有aliyun.oss.endpoint属性,有才加载这个类并交给IOC容器
public class AliOssConfiguration {

    @Autowired
    private AliOssProperties aliOssProperties;
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil() {
        log.info("开始创建阿里云文件上传工具类对象: {}", aliOssProperties);

        return new AliOssUtil(aliOssProperties.getEndpoint(),
                    aliOssProperties.getAccessKeyId(),
                    aliOssProperties.getAccessKeySecret(),
                    aliOssProperties.getBucketName());
    }
}
