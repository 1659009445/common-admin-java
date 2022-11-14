package com.huii.admin.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.admin.common.lang.dto.MenuNavDto;
import com.huii.admin.modules.system.entity.SysMenu;
import com.huii.admin.modules.system.mapper.SysMenuMapper;
import com.huii.admin.modules.system.mapper.SysRoleMapper;
import com.huii.admin.modules.system.mapper.SysUserMapper;
import com.huii.admin.modules.system.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<MenuNavDto> getMenuList(Long id) {
        List<SysMenu> menuList = sysUserMapper.getMenuEntityByUserId(id);
        List<SysMenu> menuTreeList = buildTreeList(menuList);

        return convertDtoList(menuTreeList, 1);
    }

    @Override
    public List<MenuNavDto> getHoleMenuList(Long id) {
        List<SysMenu> menuList = sysMenuMapper.selectList(null);
        List<SysMenu> menuTreeList = buildTreeList(menuList);

        return convertDtoList(menuTreeList,0);
    }

    @Override
    public List<SysMenu> getTreeList() {
        List<SysMenu> sysMenus = this.list(new QueryWrapper<SysMenu>().orderByAsc("orderNum"));
        return buildTreeList(sysMenus);
    }

    @Override
    public Boolean delSysRoleR(Long id) {
        return sysRoleMapper.delSysRoleR(id);
    }

    /**
     * 生成树状结构
     * @param menuList menuList
     * @return treeList
     */
    private List<SysMenu> buildTreeList(List<SysMenu> menuList) {

        List<SysMenu> myList = new ArrayList<>();

        menuList.sort(Comparator.comparing(SysMenu::getOrderNum));

        //查找子节点,只支持二级菜单
        for (SysMenu i : menuList){
            for(SysMenu j : menuList){
                if(Objects.equals(i.getId(), j.getPid())){
                    i.getChildren().add(j);
                }
            }
        }
        for (SysMenu i : menuList){
            //查找公共节点
            if(i.getPid() == 0L){
                myList.add(i);
            }
        }

        return  myList;
    }

    /**
     * 转换dto
     * @param type type为1 表示生成nav菜单 只有getIsTitle == 1 时 加入list  type为0时 表示生成新增菜单 此时全部生成
     * @param menuTreeList menuTreeList
     * @return MenuNavDtoList
     */
    private List<MenuNavDto> convertDtoList(List<SysMenu> menuTreeList,int type) {
        List<MenuNavDto> myList = new ArrayList<>();

        menuTreeList.forEach(i ->{
            MenuNavDto dto = new MenuNavDto();

            dto.setId(i.getId());
            dto.setIcon(i.getIcon());
            dto.setName(i.getName());
            dto.setPath(i.getUrl());
            dto.setTitle(i.getName());
            dto.setComponent(i.getComponent());

            if(i.getChildren().size() > 0){
                dto.setChildren(convertDtoList(i.getChildren(),type));
            }
            if(type == 1 && i.getIsTitle()==1){
                myList.add(dto);
            } else if (type == 0){
                myList.add(dto);
            }
        });

        return myList;
    }
}
