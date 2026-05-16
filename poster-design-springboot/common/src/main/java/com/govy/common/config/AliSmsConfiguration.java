package com.govy.common.config;

import com.govy.common.properties.AliSmsProperties;
import com.govy.common.utils.AliSmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "aliyun.sms.endpoint")  // 判断配置文件中是否有aliyun.sms
@Slf4j
// .endpoint属性,有才加载这个类并交给IOC容器
public class AliSmsConfiguration {

    @Autowired
    private AliSmsProperties aliSMSProperties;

    @Bean
    public AliSmsUtil aliSmsUtil() {
        log.info("开始创建阿里云短信发送工具类对象: {}", aliSMSProperties);
        return new AliSmsUtil(aliSMSProperties.getAccessKeyId(),
                aliSMSProperties.getAccessKeySecret(),
                aliSMSProperties.getEndpoint()
        );
    }
}
