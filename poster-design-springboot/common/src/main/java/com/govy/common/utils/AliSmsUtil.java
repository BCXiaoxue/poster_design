package com.govy.common.utils;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.teaopenapi.models.Config;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


@Data
@AllArgsConstructor
@Slf4j
public class AliSmsUtil {

    private String accessKeyId;

    private String accessKeySecret;

    private String endpoint;

    private static final String STRING_NAME = "阿里云短信测试";
    private static final String TEMPLATE_CODE = "SMS_154950909";
    @SneakyThrows
    public void sendCode(String phone, String code) {
        Config config = new Config();
        config.setAccessKeyId(accessKeyId);
        config.setAccessKeySecret(accessKeySecret);
        config.setEndpoint(endpoint);

        Client client = new Client(config);

        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(phone);    // 目标手机号
        sendSmsRequest.setSignName(STRING_NAME);   // 签名
        sendSmsRequest.setTemplateCode(TEMPLATE_CODE);  // 消息模板
        sendSmsRequest.setTemplateParam("{\"code\":\"" + code + "\"}");  // 发送内容, 用json字符串传递

        try {
            client.sendSms(sendSmsRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
