package com.huii.admin.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huii.admin.common.lang.dto.MenuNavDto;
import com.huii.admin.modules.system.entity.SysMenu;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    /**
     * 获取登录后菜单列表
     * @param id userId
     * @return list
     */
    List<MenuNavDto> getMenuList(Long id);

    /**
     * 获取全部菜单信息
     * @param id userId
     * @return list
     */
    List<MenuNavDto> getHoleMenuList(Long id);

    /**
     * 获取菜单集合 已转为树状结构
     * @return list
     */
    List<SysMenu> getTreeList();

    /**
     * 删除SYS_ROLE_MENU关联表信息
     * @param id menuId
     * @return boolean
     */
    Boolean delSysRoleR(Long id);


}
