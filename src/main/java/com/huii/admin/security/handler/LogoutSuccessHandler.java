package com.huii.admin.security.handler;

import com.alibaba.fastjson.JSON;
import com.huii.admin.common.annotation.LoginLog;
import com.huii.admin.common.enums.ResultCode;
import com.huii.admin.common.lang.Const;
import com.huii.admin.common.result.Result;
import com.huii.admin.common.utils.RedisTemplateUtil;
import com.huii.admin.security.util.JwtAuthUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Autowired
    private JwtAuthUtil jwtAuthUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String uri = request.getRequestURI();
        if ("/logout".equals(uri) && request.getMethod().equals("POST")) {
            String token = request.getHeader("token");
            String id = jwtAuthUtil.parseIdByTokenToString(token);
            if(!StringUtils.hasText(id)){
                throw new JwtException(ResultCode.UNAUTHORIZED.getMessage());
            }

            SecurityContextHolder.getContext().setAuthentication(null);

            redisTemplateUtil.deleteObject(Const.USER_PREFIX+id);
            redisTemplateUtil.deleteObject(Const.TOKEN_PREFIX+id);

            response.setStatus(200);
            response.setContentType("application/json;charset=UTF-8");

            String result = JSON.toJSONString(Result.success(null));

            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(result.getBytes(StandardCharsets.UTF_8));
            outputStream.close();
        }
    }
}
