package com.huii.admin.security.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.huii.admin.modules.system.entity.SysUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserDetail implements UserDetails {

    private SysUser user;

    private List<String> auths;

    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;

    public UserDetail(SysUser user, List<String> auths) {
        this.user = user;
        this.auths = auths;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        authorities = auths.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        //0 账户正常  1账户被禁用
        return user.getIsBanned() == 0;
    }
}
