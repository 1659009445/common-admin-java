package com.huii.admin.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huii.admin.modules.system.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户ID查找角色集合 (一个用户可能有多个角色)
     * @param id userID
     * @return List
     */
    List<SysRole> selectRoleByUserId(Long id);

    /**
     * 根据角色ID查找用户集合
     * @param id userID
     * @return List
     */
    List<Long> selectUserIdByRoleId(Long id);

    /**
     * 根据菜单ID删除S_Role_Menu关联表中的信息
     * @param id menuID
     * @return boolean
     */
    Boolean delSysRoleR(Long id);

}
