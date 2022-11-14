package com.huii.admin.security.service;

import com.huii.admin.common.enums.ResultCode;
import com.huii.admin.common.exception.NormalException;
import com.huii.admin.modules.system.entity.SysUser;
import com.huii.admin.modules.system.service.SysUserService;
import com.huii.admin.security.entity.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser user = sysUserService.getUserByUsername(username);

        if(Objects.isNull(user)){
            throw new NormalException(ResultCode.LOGIN_ACCOUNT_ERROR);
        }

        List<String> auths = sysUserService.getAuthority(user.getId());

        return new UserDetail(user,auths);
    }
}
