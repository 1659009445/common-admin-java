package com.huii.admin.security.handler;

import com.alibaba.fastjson.JSON;
import com.huii.admin.common.annotation.LoginLog;
import com.huii.admin.common.enums.ResultCode;
import com.huii.admin.common.result.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    @LoginLog(value = "登录失败",isLoginSuccess = false)
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");

        String msg = (String) request.getAttribute("kaptcha_mismatch");
        String result = JSON.toJSONString(Result.failed(StringUtils.isNotBlank(msg) ? msg : ResultCode.LOGIN_ACCOUNT_ERROR.getMessage()));

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }
}
