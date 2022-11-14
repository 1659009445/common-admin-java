package com.huii.admin.security.filter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.huii.admin.common.enums.ResultCode;
import com.huii.admin.common.exception.KaptchaException;
import com.huii.admin.common.lang.Const;
import com.huii.admin.common.utils.RedisTemplateUtil;
import com.huii.admin.security.handler.LoginFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginKaptchaFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Autowired
    private LoginFailureHandler loginFailureHandler;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();

        if ("/login".equals(uri) && request.getMethod().equals("POST")) {
            try {
                validate(request);
            }catch (KaptchaException e){
                request.setAttribute("kaptcha_mismatch",e.getMessage());
                loginFailureHandler.onAuthenticationFailure(request, response, e);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void validate(HttpServletRequest request) {
        //uuid
        String key = request.getParameter("key");
        //验证码
        String kaptcha = request.getParameter("code");

        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(key)) {
            throw new KaptchaException(ResultCode.LOGIN_CODE_ERROR.getMessage());
        }
        String code = redisTemplateUtil.getCacheObject(Const.KAPTCHA_PREFIX + key);
        if (!kaptcha.equals(code)||StringUtils.isBlank(code)) {
            throw new KaptchaException(ResultCode.LOGIN_CODE_ERROR.getMessage());
        }
        redisTemplateUtil.deleteObject(Const.KAPTCHA_PREFIX + key);
    }
}
