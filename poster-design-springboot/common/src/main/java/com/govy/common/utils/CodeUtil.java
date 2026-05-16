package com.govy.common.utils;


import java.util.Random;

// 获取验证码的静态方法
public class CodeUtil {

    public static String getRandomCode(Integer len) {
        StringBuilder stringBuilder = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < len; i++) {
            int num = r.nextInt(10);
            stringBuilder.append(num);
        }
        return stringBuilder.toString();
    }
}
