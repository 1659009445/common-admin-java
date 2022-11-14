package com.huii.admin.common.exception;

import com.huii.admin.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<?> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        ObjectError objectError = result.getAllErrors().stream().findFirst().get();
        log.error("RRException:"+objectError.getDefaultMessage());
        return Result.failed(objectError.getDefaultMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result<?> IllegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.error("AssertException:"+e.getMessage());
        return Result.failed(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = NormalException.class)
    public Result<?> NormalExceptionHandler(NormalException e){
        log.error("NormalException:"+e.getErrorMsg());
        return Result.failed(e.getErrorCode(), e.getErrorMsg());
    }

    @ResponseBody
    @ExceptionHandler(value = NullPointerException.class)
    public Result<?> NullPointerExceptionHandler(RuntimeException e){
        log.error("NullPointerException:"+e.getMessage());
        return Result.failed("500","服务器繁忙!");
    }

    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    public Result<?> RuntimeExceptionHandler(RuntimeException e){
        log.error("RuntimeException:"+e.getMessage());
        return Result.failed("500",e.getMessage());
    }

}
