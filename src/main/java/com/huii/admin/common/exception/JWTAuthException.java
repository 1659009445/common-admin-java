package com.huii.admin.common.exception;

import io.jsonwebtoken.JwtException;

public class JWTAuthException extends JwtException {
    public JWTAuthException(String message) {
        super(message);
    }
}
