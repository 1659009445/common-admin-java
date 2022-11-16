package com.huii.admin.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.admin.common.exception.NormalException;
import com.huii.admin.common.lang.Const;
import com.huii.admin.common.lang.dto.PasswordDto;
import com.huii.admin.common.lang.dto.ResetPasswordDto;
import com.huii.admin.common.lang.dto.UserBasicInfoDto;
import com.huii.admin.common.utils.PageUtil;
import com.huii.admin.common.utils.QueryUtil;
import com.huii.admin.common.utils.RedisTemplateUtil;
import com.huii.admin.modules.system.entity.SysUser;
import com.huii.admin.modules.system.mapper.SysRoleMapper;
import com.huii.admin.modules.system.mapper.SysUserMapper;
import com.huii.admin.modules.system.service.SysUserService;
import com.huii.admin.security.entity.UserDetail;
import com.huii.admin.security.util.JwtAuthUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private JwtAuthUtil jwtAuthUtil;

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Lazy
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public SysUser getUserByUsername(String username) {
        return getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername,username));
    }

    @Override
    public List<String> getAuthority(Long userId) {
        return sysUserMapper.getMenuPermsByUserId(userId);
    }

    @Override
    public Boolean clearUserAuthorityInfo(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //重新获取UserDetail对象
        SysUser user = getUserByUsername(sysUserMapper.selectById(userId).getUsername());
        List<String> auths = getAuthority(userId);
        UserDetail userDetail = new UserDetail(user,auths);

        //封装Authentication对象
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //更新redis数据
        //redisTemplateUtil.deleteObject(Const.USER_PREFIX+userId);
        redisTemplateUtil.setCacheObject(Const.USER_PREFIX+userId, authenticationToken.getPrincipal(), jwtAuthUtil.getSINGLE_TIME(), TimeUnit.HOURS);
        return true;
    }

    @Override
    public Boolean clearUserAuthorityInfoByRoleId(Long roleId) {
        List<Long> idList = sysRoleMapper.selectUserIdByRoleId(roleId);
        for (Long aLong : idList) {
            clearUserAuthorityInfo(aLong);
        }
        return true;
    }

    @Override
    public Boolean clearUserAuthorityInfoByMenuId(Long menuId) {
        List<Long> idList = sysUserMapper.selectUserIdByMenuId(menuId);
        for (Long aLong : idList) {
            clearUserAuthorityInfo(aLong);
        }
        return true;
    }

    @Override
    public UserBasicInfoDto transformDto(SysUser user) {
        UserBasicInfoDto dto = new UserBasicInfoDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setSexual(user.getSexual());
        dto.setAvatar(user.getAvatar());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setCreateTime(user.getCreateTime());
        dto.setLastLoginTime(user.getLastLoginTime());
        dto.setLastLoginIp(user.getLastLoginIp());
        return dto;
    }

    @Override
    public PageUtil queryList(Map<String, Object> params) {

        QueryUtil<SysUser> queryUtil = new QueryUtil<>();
        IPage<SysUser> iPage = queryUtil.getPageInfo(params);

        String param = (String) params.get("param");
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(param),SysUser::getUsername,param);

        IPage<SysUser> page = this.page(iPage,wrapper);

        page.getRecords().forEach(i ->{
            i.setRoleList(sysRoleMapper.selectRoleByUserId(i.getId()));
        });

        return new PageUtil(page);
    }

    @Override
    public SysUser initUserInfo(SysUser sysUser) {
        sysUser.setIsBanned(0);
        sysUser.setIsDeleted(0);
        sysUser.setCreateTime(LocalDateTime.now());
        sysUser.setAvatar(Const.USER_DEFAULT_AVATAR);
        sysUser.setSexual(Const.USER_DEFAULT_SEXUAL);
        sysUser.setPassword(bCryptPasswordEncoder.encode(Const.USER_DEFAULT_PASSWORD));
        return sysUser;
    }

    @Override
    public Boolean updatePassByAdmin(Long id) {
        System.out.println(id);
        System.out.println(1);
        SysUser sysUser = sysUserMapper.selectById(id);
        sysUser.setPassword(bCryptPasswordEncoder.encode(Const.USER_DEFAULT_PASSWORD));
        sysUserMapper.updateById(sysUser);
        return true;
    }

    @Override
    public Boolean updatePassByUser(Long id, PasswordDto dto) {
        SysUser sysUser = sysUserMapper.selectById(id);

        if(bCryptPasswordEncoder.matches(dto.getOldPass(), sysUser.getPassword())){
            throw new RuntimeException("请输入正确的原来的密码!");
        }

        sysUser.setPassword(bCryptPasswordEncoder.encode(dto.getNewPass()));
        sysUserMapper.updateById(sysUser);
        return true;
    }

    @Override
    public Boolean updateForgetPass(ResetPasswordDto dto) {
        if(!dto.getPassword().equals( dto.getConfirm())){
            throw new NormalException("清确保两次输入的密码一致!");
        }
        String redis_code = (String)redisTemplateUtil.getCacheObject(Const.RESET_PASS_CODE_PREFIX + dto.getKey());
        if(!redis_code.equals(dto.getCode())){
            throw new NormalException("验证码错误!");
        }
        SysUser user1 = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername,dto.getUsername()));
        if(ObjectUtils.isEmpty(user1) || !user1.getEmail().equals(dto.getEmail())){
            throw new NormalException("用户不存在!");
        }
        user1.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        sysUserMapper.updateById(user1);
        return true;
    }

    @Override
    public List<SysUser> getList() {
        return sysUserMapper.selectList(null);
    }

    @Override
    public Boolean confirmOldPassword(Long id, String used) {
        SysUser sysUser = sysUserMapper.selectById(id);
        String password = sysUser.getPassword();
        return bCryptPasswordEncoder.matches(used,password);
    }

    @Override
    public Boolean updateNewPassword(Long id, String password) {
        SysUser sysUser = sysUserMapper.selectById(id);
        String encode = bCryptPasswordEncoder.encode(password);
        sysUser.setPassword(encode);
        return sysUserMapper.updateById(sysUser)>0;
    }


}
