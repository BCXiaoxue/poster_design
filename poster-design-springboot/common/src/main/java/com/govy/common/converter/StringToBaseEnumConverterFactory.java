package com.govy.common.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

//@Component
//public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {
//    @Override
//    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
//        return new Converter<String, T>() {
//            @Override
//            public T convert(String code) {
//                try {
//                    int codeValue = Integer.parseInt(code);
//                    for (T t : targetType.getEnumConstants()) {
//                        if (t.getCode() == codeValue) {
//                            return t;
//                        }
//                    }
//                } catch (NumberFormatException e) {
//                    throw new IllegalArgumentException("code 必须是整数: " + code, e);
//                }
//                throw new IllegalArgumentException("未找到匹配的枚举类型: " + code);
//            }
//        };
//    }
//}
