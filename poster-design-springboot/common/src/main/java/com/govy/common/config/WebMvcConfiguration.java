package com.govy.common.config;


//import com.govy.common.converter.StringToBaseEnumConverterFactory;
import com.govy.common.interceptor.AuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;
//    private final StringToBaseEnumConverterFactory stringToBaseEnumConverterFactory;

    /**
     * 添加枚举转换器
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        log.info("添加枚举转换器...");
//        registry.addConverterFactory(stringToBaseEnumConverterFactory);
    }

    /**
     * 添加拦截器
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("添加拦截器...");
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login/**")
                .excludePathPatterns("/user/register/**")
                .excludePathPatterns("/user/send-code/**")
                .excludePathPatterns("/user/university/**")
                .excludePathPatterns("/user/updateByPhone/**")
                .excludePathPatterns("/user/loginByCode/**")
                .excludePathPatterns("/annunciation/queryList")
                .excludePathPatterns("/news/queryList")
                .excludePathPatterns("/competition")
                .excludePathPatterns("/award/getWinWithCompetition")
                // 以下是添加的Knife4j（Swagger）相关排除路径
                .excludePathPatterns("/doc.html")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/v2/api-docs")
                .excludePathPatterns("/v3/api-docs/**")
                .excludePathPatterns("/favicon.ico");
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/doc.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }
}
