package com.huii.admin.security.filter;

import com.huii.admin.common.enums.ResultCode;
import com.huii.admin.common.exception.JWTAuthException;
import com.huii.admin.common.lang.Const;
import com.huii.admin.common.utils.RedisTemplateUtil;
import com.huii.admin.security.entity.UserDetail;
import com.huii.admin.security.util.JwtAuthUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtAuthUtil jwtAuthUtil;

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        String token = request.getHeader("token");

        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String id = jwtAuthUtil.parseIdByTokenToString(token);
        if(!StringUtils.hasText(id)){
            throw new JWTAuthException(ResultCode.UNAUTHORIZED.getMessage());
        }
        request.setAttribute(Const.USER_ID,id);
        UserDetail userDetail = redisTemplateUtil.getCacheObject(Const.USER_PREFIX + id);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
