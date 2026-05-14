package com.dormitory.config;

import com.dormitory.common.LoginException;
import com.dormitory.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginException.class)
    public Result<?> handleLogin(LoginException e) {
        return Result.fail(401, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleOther(Exception e) {
        return Result.fail(e.getMessage());
    }
}
