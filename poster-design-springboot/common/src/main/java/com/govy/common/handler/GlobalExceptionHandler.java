package com.govy.common.handler;

import com.govy.common.constant.ResultCodeConstant;
import com.govy.common.constant.ResultConstant;
import com.govy.common.result.Result;
import com.govy.common.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler{

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        log.error("发生未捕获异常：{}", e.getMessage(), e);
        return Result.error(ResultConstant.FAIL);
    }

    @ExceptionHandler(BaseException.class)
    public Result<String> handleLeaseException(BaseException e) {
        log.error("发生业务异常：{}", e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("发生参数校验异常：{}", e.getMessage(), e);
        return Result.error(ResultCodeEnum.PARAM_ERROR,
                e.getBindingResult().getFieldError().getDefaultMessage());
    }


}
