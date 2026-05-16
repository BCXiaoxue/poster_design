package com.govy.common.interceptor;

import com.govy.common.handler.BaseException;

import com.govy.common.login.LoginUser;
import com.govy.common.login.LoginUserHolder;
import com.govy.common.result.ResultCodeEnum;
import com.govy.common.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        // 获取请求头中的 token
        String token = request.getHeader("Authentication");
//        log.info("token: {}", token);
        if (token == null || token.isEmpty()) {
            throw new BaseException(ResultCodeEnum.TOKEN_NOT_EXIST);
        }

        // 解析 token
        Map<String, Object> map = JwtUtil.parseJwt(token);
        LoginUser loginUser = new LoginUser(Integer.parseInt(map.get("id").toString()), map.get("username").toString());
        LoginUserHolder.setLoginUser(loginUser);

        return true;
    }

    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        LoginUserHolder.clear();
    }
}
