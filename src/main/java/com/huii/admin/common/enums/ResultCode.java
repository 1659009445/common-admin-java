package com.huii.admin.common.enums;

public enum ResultCode {

    SUCCESS("200", "操作成功"),
    FAILED("500", "操作失败"),

    VALIDATE_FAILED("404", "参数检验失败"),
    UNAUTHORIZED("401", "暂未登录或token已经过期"),
    FORBIDDEN("403", "没有相关权限"),
    LOGIN_ACCOUNT_ERROR("500", "账号或密码错误"),
    LOGIN_CODE_ERROR("500", "验证码错误"),

    ;

    private final String code;

    private final String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
