package com.huii.admin.security.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.huii.admin.common.lang.Const;
import com.huii.admin.common.lang.dto.LoginUserInformDto;
import com.huii.admin.common.result.Result;
import com.huii.admin.common.utils.IpAddressUtil;
import com.huii.admin.common.utils.RedisTemplateUtil;
import com.huii.admin.modules.system.entity.SysUser;
import com.huii.admin.modules.system.mapper.SysUserMapper;
import com.huii.admin.security.entity.UserDetail;
import com.huii.admin.security.util.JwtAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private JwtAuthUtil jwtAuthUtil;

    @Autowired
    private IpAddressUtil ipAddressUtil;

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        //获取用户信息并生成token
        UserDetail principal = (UserDetail)authentication.getPrincipal();
        Long id = principal.getUser().getId();
        String token = jwtAuthUtil.createNormalToken(id);
        //存入redis
        redisTemplateUtil.setCacheObject(Const.TOKEN_PREFIX+id, token, jwtAuthUtil.getSINGLE_TIME(), TimeUnit.HOURS);
        redisTemplateUtil.setCacheObject(Const.USER_PREFIX+id, principal, jwtAuthUtil.getSINGLE_TIME(), TimeUnit.HOURS);
        //获取并更新用户信息
        SysUser user = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getId, id)
                .select(SysUser::getId, SysUser::getUsername, SysUser::getLastLoginTime, SysUser::getLastLoginIp));
        user.setLastLoginIp(ipAddressUtil.getIpAddress(request));
        user.setLastLoginTime(LocalDateTime.now());
        sysUserMapper.updateById(user);

        //TODO 登录日志记录

        //封装登录信息并返回
        LoginUserInformDto dto = new LoginUserInformDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setLastLoginIP(ipAddressUtil.getIpAddress(request));
        dto.setLastLoginTime(LocalDateTime.now());

        Map<String,Object> map = new HashMap<>();
        map.put("info",dto);
        map.put("token",token);
        String result = JSON.toJSONString(Result.success(map));
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
