package com.huii.admin.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huii.admin.common.lang.Const;
import com.huii.admin.common.result.Result;
import com.huii.admin.common.utils.PageUtil;
import com.huii.admin.modules.system.entity.SysRole;
import com.huii.admin.modules.system.entity.SysRoleMenu;
import com.huii.admin.modules.system.entity.SysUserRole;
import com.huii.admin.modules.system.service.SysRoleMenuService;
import com.huii.admin.modules.system.service.SysRoleService;
import com.huii.admin.modules.system.service.SysUserRoleService;
import com.huii.admin.modules.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "role")
@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysRoleMenuService sysRoleMenuService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @ApiOperation("查询单例")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:role:query')")
    public Result<SysRole> getOne(@PathVariable("id") Long id) {
        SysRole sysRole = sysRoleService.getById(id);

        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(new LambdaQueryWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getRoleId, id)
                .select(SysRoleMenu::getMenuId));
        List<Long> menuIdList = sysRoleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());

        sysRole.setMenuIdList(menuIdList);

        return Result.success(sysRole);
    }

    @ApiOperation("查询集合")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:role:query')")
    public Result<PageUtil> getList(@ApiParam("param ?, current, pageSize") @RequestParam Map<String, Object> params) {
        PageUtil page = sysRoleService.queryList(params);
        return Result.success(page);
    }

    @ApiOperation("保存")
    @PostMapping("/insert")
    @PreAuthorize("hasAuthority('sys:role:insert')")
    public Result<SysRole> save(@Validated @RequestBody SysRole sysRole) {
        sysRole.setCreated(LocalDateTime.now());
        sysRole.setUpdated(LocalDateTime.now());
        sysRole.setStatus(Const.STATUS_ON);
        sysRoleService.save(sysRole);
        return Result.success(sysRole);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public Result<SysRole> update(@Validated @RequestBody SysRole sysRole) {
        sysRole.setUpdated(LocalDateTime.now());
        sysRoleService.updateById(sysRole);
        sysUserService.clearUserAuthorityInfoByRoleId(sysRole.getId());
        return Result.success(sysRole);
    }

    @Transactional
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public Result<Object> delete(@RequestBody Long[] ids) {
        sysRoleService.removeBatchByIds(Arrays.asList(ids));
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().in(SysRoleMenu::getRoleId,ids));
        sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getRoleId,ids));

        Arrays.stream(ids).forEach(id ->{
            sysUserService.clearUserAuthorityInfoByRoleId(id);
        });

        return Result.success(null);
    }

    @Transactional
    @ApiOperation("修改角色权限")
    @PostMapping("/perm/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public Result<Long[]> getMenus(@PathVariable("roleId") Long roleId, @RequestBody Long[] menuIds) {

        List<SysRoleMenu> sysRoleMenus = new ArrayList<>();

        Arrays.stream(menuIds).forEach(id ->{
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(id);
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenus.add(sysRoleMenu);
        });

        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getRoleId,roleId));
        sysRoleMenuService.saveBatch(sysRoleMenus);

        sysUserService.clearUserAuthorityInfoByRoleId(roleId);

        return Result.success(menuIds);
    }
}
