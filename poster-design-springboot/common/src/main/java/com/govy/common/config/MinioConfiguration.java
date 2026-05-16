package com.govy.common.config;

import com.govy.common.properties.MinioProperties;
import com.govy.common.utils.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@ConditionalOnProperty(name = "govy.minio.endpoint")   // 判断配置文件中是否有govy.minio.endpoint属性,有才加载这个类并交给IOC容器
public class MinioConfiguration {

    @Autowired
    private MinioProperties minioProperties;

    @Bean
    public MinioUtil minioClient() {
        log.info("开始创建Minio文件上传工具类对象: {}", minioProperties);
         return new MinioUtil(minioProperties.getEndpoint(),
                 minioProperties.getAccessKey(),
                 minioProperties.getSecretKey(),
                 minioProperties.getBucketName()
         );
    }
}
