package com.huii.admin.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huii.admin.modules.system.entity.SysMenu;
import com.huii.admin.modules.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户ID查找菜单ID集合
     * @param id userID
     * @return List
     */
    List<Long> getMenuIdsByUserId(Long id);

    /**
     * 根据用户ID查找菜单实体集合
     * @param id userID
     * @return List
     */
    List<SysMenu> getMenuEntityByUserId(Long id);

    /**
     * 根据用户ID获取菜单权限集合
     * @param id userID
     * @return List
     */
    List<String> getMenuPermsByUserId(Long id);

    /**
     * 通过菜单ID查找用户集合
     * @param id userID
     * @return List
     */
    List<Long> selectUserIdByMenuId(Long id);

}
