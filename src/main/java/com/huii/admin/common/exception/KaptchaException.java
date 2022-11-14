package com.huii.admin.common.exception;

import org.springframework.security.core.AuthenticationException;

public class KaptchaException extends AuthenticationException {
    public KaptchaException(String msg) {
        super(msg);
    }
}
