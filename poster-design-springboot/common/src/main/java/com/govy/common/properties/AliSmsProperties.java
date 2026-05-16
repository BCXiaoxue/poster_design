package com.govy.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "aliyun.sms")
public class AliSmsProperties {

    private String accessKeyId;

    private String accessKeySecret;

    private String endpoint;
}
