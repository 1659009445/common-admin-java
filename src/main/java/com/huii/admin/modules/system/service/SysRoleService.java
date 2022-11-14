package com.huii.admin.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huii.admin.common.utils.PageUtil;
import com.huii.admin.modules.system.entity.SysRole;

import java.util.List;
import java.util.Map;

public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页查询
     * @param params args
     * @return list
     */
    PageUtil queryList(Map<String, Object> params);

    /**
     * 查询用户的角色列表
     * @param id userId
     * @return list
     */
    List<SysRole> getRoleListByUserId(Long id);
}
