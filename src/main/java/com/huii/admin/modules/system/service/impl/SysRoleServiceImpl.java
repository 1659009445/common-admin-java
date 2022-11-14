package com.huii.admin.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.admin.common.utils.PageUtil;
import com.huii.admin.common.utils.QueryUtil;
import com.huii.admin.modules.system.entity.SysRole;
import com.huii.admin.modules.system.entity.SysRoleMenu;
import com.huii.admin.modules.system.mapper.SysRoleMapper;
import com.huii.admin.modules.system.mapper.SysRoleMenuMapper;
import com.huii.admin.modules.system.service.SysRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService{

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public PageUtil queryList(Map<String, Object> params) {
        QueryUtil<SysRole> queryUtil = new QueryUtil<>();
        IPage<SysRole> iPage = queryUtil.getPageInfo(params);

        String param = (String) params.get("param");
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(param),SysRole::getName,param);

        IPage<SysRole> page = this.page(iPage,wrapper);

        page.getRecords().forEach(i -> {
            List<SysRoleMenu> menus = sysRoleMenuMapper.selectList(new LambdaQueryWrapper<SysRoleMenu>()
                    .eq(SysRoleMenu::getRoleId,i.getId())
                    .select(SysRoleMenu::getMenuId));
            List<Long> ids = new ArrayList<>();
            menus.forEach(j ->{
                ids.add(j.getMenuId());
            });
            i.setMenuIdList(ids);
        });

        return new PageUtil(page);
    }

    @Override
    public List<SysRole> getRoleListByUserId(Long id) {
        return sysRoleMapper.selectRoleByUserId(id);
    }
}
