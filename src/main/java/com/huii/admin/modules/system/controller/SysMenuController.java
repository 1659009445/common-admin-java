package com.huii.admin.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huii.admin.common.exception.NormalException;
import com.huii.admin.common.lang.Const;
import com.huii.admin.common.lang.dto.MenuNavDto;
import com.huii.admin.common.result.Result;
import com.huii.admin.modules.system.entity.SysMenu;
import com.huii.admin.modules.system.service.SysMenuService;
import com.huii.admin.modules.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "menu")
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Autowired
    HttpServletRequest request;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysMenuService sysMenuService;


    @ApiOperation("获取菜单导航栏")
    @GetMapping("/nav")
    public Result<Map<String, Object>> getMenuList(){
        Long id = Long.parseLong((String) request.getAttribute(Const.USER_ID));

        List<String> authority = sysUserService.getAuthority(id);
        List<MenuNavDto> list = sysMenuService.getMenuList(id);

        Map<String, Object> map = new HashMap<>();
        map.put("authority",authority);
        map.put("menuList",list);

        return Result.success(map);
    }

    @ApiOperation("查询单例")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:menu:query')")
    public Result<SysMenu> getOne(@PathVariable(name = "id") Long id){
        return Result.success(sysMenuService.getById(id));
    }

    @ApiOperation("查询全部")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:menu:query')")
    public Result<List<SysMenu>> getList(){
        return Result.success(sysMenuService.getTreeList());
    }

    @ApiOperation("保存")
    @PostMapping("/insert")
    @PreAuthorize("hasAuthority('sys:menu:insert')")
    public Result<SysMenu> insert(@Validated @RequestBody SysMenu sysMenu){
        sysMenu.setCreated(LocalDateTime.now());
        sysMenu.setUpdated(LocalDateTime.now());
        sysMenuService.save(sysMenu);

        return Result.success(sysMenu);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    public Result<SysMenu> update(@Validated @RequestBody SysMenu sysMenu){
        sysMenu.setUpdated(LocalDateTime.now());
        sysMenuService.updateById(sysMenu);
        sysUserService.clearUserAuthorityInfoByMenuId(sysMenu.getId());

        return Result.success(sysMenu);
    }

    @ApiOperation("删除")
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public Result<Object> delete(@PathVariable("id") Long id){
        int count = (int) sysMenuService.count(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getPid, id));
        if (count > 0) {
            throw new NormalException("请先删除子菜单!");
        }

        sysUserService.clearUserAuthorityInfoByMenuId(id);
        sysMenuService.removeById(id);
        sysMenuService.delSysRoleR(id);

        return Result.success(null);
    }
}
