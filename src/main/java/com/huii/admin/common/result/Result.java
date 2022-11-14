package com.huii.admin.common.result;

import com.huii.admin.common.enums.ResultCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private String code;

    private String message;

    private T data;

    protected Result() {

    }

    protected Result(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     */
    public static <T> Result<T> success(T data){
        return new Result<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(),data);
    }

    public static <T> Result<T> success(String message, T data){
        return new Result<T>(ResultCode.SUCCESS.getCode(), message, data);
    }

    public static <T> Result<T> success(String code, String message, T data){
        return new Result<T>(code, message, data);
    }

    public static <T> Result<T> success(ResultCode resultCode, T data){
        return new Result<T>(resultCode.getCode(), resultCode.getMessage(), data);
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> failed(){
        return new Result<T>(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage(), null);
    }

    public static <T> Result<T> failed(String message){
        return new Result<T>(ResultCode.FAILED.getCode(), message, null);
    }

    public static <T> Result<T> failed(String code, String message){
        return new Result<T>(code, message, null);
    }

    public static <T> Result<T> failed(ResultCode resultCode){
        return new Result<T>(resultCode.getCode(), resultCode.getMessage(), null);
    }

}
