package com.huii.admin.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huii.admin.common.lang.dto.PasswordDto;
import com.huii.admin.common.lang.dto.ResetPasswordDto;
import com.huii.admin.common.lang.dto.UserBasicInfoDto;
import com.huii.admin.common.utils.PageUtil;
import com.huii.admin.modules.system.entity.SysUser;

import java.util.List;
import java.util.Map;

public interface SysUserService extends IService<SysUser> {

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return SysUser对象
     */
    SysUser getUserByUsername(String username);

    /**
     * 根据ID获取用户权限列表
     * @param userId id
     * @return list
     */
    List<String> getAuthority(Long userId);

    /**
     * 更新权限数据
     * @param userId userId
     * @return boolean
     */
    Boolean clearUserAuthorityInfo(Long userId);

    /**
     * 更新权限数据
     * @param roleId roleId
     * @return boolean
     */
    Boolean clearUserAuthorityInfoByRoleId(Long roleId);

    /**
     * 更新权限数据
     * @param menuId menuId
     * @return boolean
     */
    Boolean clearUserAuthorityInfoByMenuId(Long menuId);

    /**
     * 获取用户信息 转换dto返回
     * @param user user
     * @return userDto
     */
    UserBasicInfoDto transformDto(SysUser user);

    /**
     * 查询用户集合
     * @param params args
     * @return list
     */
    PageUtil queryList(Map<String, Object> params);

    /**
     * 创建用户初始化啊
     * @param sysUser user
     * @return user
     */
    SysUser initUserInfo(SysUser sysUser);

    /**
     * 管理员重置密码
     * @param id userId
     * @return boolean
     */
    Boolean updatePassByAdmin(Long id);

    /**
     * 用户更新密码
     * @param id id
     * @param dto old_pass, new_pass
     * @return boolean
     */
    Boolean updatePassByUser(Long id, PasswordDto dto);

    /**
     * 用户忘记密码
     * @param dto ResetPasswordDto
     * @return boolean
     */
    Boolean updateForgetPass(ResetPasswordDto dto);
}
