package com.huii.admin.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huii.admin.common.annotation.Log;
import com.huii.admin.common.exception.NormalException;
import com.huii.admin.common.lang.Const;
import com.huii.admin.common.lang.dto.PasswordConfirmDto;
import com.huii.admin.common.lang.dto.PasswordDto;
import com.huii.admin.common.lang.dto.ResetPasswordDto;
import com.huii.admin.common.lang.dto.UserBasicInfoDto;
import com.huii.admin.common.result.Result;
import com.huii.admin.common.utils.ExcelUtil;
import com.huii.admin.common.utils.PageUtil;
import com.huii.admin.modules.system.entity.SysRole;
import com.huii.admin.modules.system.entity.SysUser;
import com.huii.admin.modules.system.entity.SysUserRole;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Api(tags = "user")
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    HttpServletRequest request;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    ExcelUtil excelUtil;

    @Log(title = "??????????????????",isSaveRequestData = false,isSaveResponseData = false)
    @ApiOperation("????????????????????????")
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('sys:common:all')")
    public Result<UserBasicInfoDto> getUserInfo(){
        Long id = Long.parseLong((String) request.getAttribute(Const.USER_ID));
        SysUser user = sysUserService.getById(id);

        return Result.success(sysUserService.transformDto(user));
    }

    @Log(title = "??????????????????",isSaveRequestData = false,isSaveResponseData = false)
    @ApiOperation("????????????")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:user:query')")
    public Result<SysUser> getOne(@PathVariable("id") Long id) {
        SysUser sysUser = sysUserService.getById(id);
        List<SysRole> roleList = sysRoleService.getRoleListByUserId(id);
        sysUser.setRoleList(roleList);

        return Result.success(sysUser);
    }

    @Log(title = "??????????????????",isSaveRequestData = false,isSaveResponseData = false)
    @ApiOperation("????????????")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:user:query')")
    public Result<PageUtil> getList(@ApiParam("param ?, current, pageSize") @RequestParam Map<String, Object> params) {
        PageUtil page = sysUserService.queryList(params);
        return Result.success(page);
    }

    @Log(title = "??????????????????")
    @ApiOperation("??????")
    @PostMapping("/insert")
    @PreAuthorize("hasAuthority('sys:user:insert')")
    public Result<SysUser> save(@Validated @RequestBody SysUser sysUser) {
        SysUser sysUser_ = sysUserService.initUserInfo(sysUser);
        sysUserService.save(sysUser_);

        return Result.success(sysUser_);
    }

    @Log(title = "??????????????????")
    @ApiOperation("??????")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:user:update')")
    public Result<SysUser> update(@Validated @RequestBody SysUser sysUser) {
        sysUserService.updateById(sysUser);
        return Result.success(sysUser);
    }

    @Log(title = "??????????????????")
    @Transactional
    @ApiOperation("??????")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public Result<Object> delete(@RequestBody Long[] ids) {

        sysUserService.removeBatchByIds(Arrays.asList(ids));
        sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>()
                .in(SysUserRole::getUserId,ids));
        return Result.success(null);
    }

    @Log(title = "????????????????????????",isSaveRequestData = false,isSaveResponseData = false)
    @Transactional
    @ApiOperation("??????????????????")
    @PostMapping("/role/{id}")
    @PreAuthorize("hasAuthority('sys:user')")
    public Result<List<SysUserRole>> getRoles(@PathVariable("id") Long id, @RequestBody Long[] roleIds) {

        List<SysUserRole> userRoles = new ArrayList<>();

        Arrays.stream(roleIds).forEach(r -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(r);
            sysUserRole.setUserId(id);

            userRoles.add(sysUserRole);
        });

        sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, id));
        sysUserRoleService.saveBatch(userRoles);

//        for( int i =0; i<roleIds.length;i++){
//            sysUserService.clearUserAuthorityInfoByRoleId(roleIds[i]);
//        }

        return Result.success(null);
    }

    @Log(title = "?????????????????????",isSaveRequestData = false,isSaveResponseData = false)
    @ApiOperation("?????????????????????")
    @PostMapping("/pass/admin/{id}")
    @PreAuthorize("hasAuthority('sys:user')")
    public Result<Object> updatePassByAdmin(@PathVariable("id") Long id) {
        sysUserService.updatePassByAdmin(id);
        return Result.success(null);
    }

    @Log(title = "??????????????????",isSaveRequestData = false,isSaveResponseData = false)
    @ApiOperation("??????????????????")
    @PostMapping("/pass/my")
    @PreAuthorize("hasAuthority('sys:common:all')")
    public Result<Object> updatePassByUser(@Validated @RequestBody PasswordDto dto) {
        Long id = Long.parseLong((String) request.getAttribute(Const.USER_ID));
        sysUserService.updatePassByUser(id, dto);

        return Result.success(null);
    }

    @Log(title = "??????????????????",isSaveRequestData = false,isSaveResponseData = false)
    @ApiOperation("??????????????????")
    @PostMapping("/pass/reset")
    public Result<Object> updatePassByEmail(@Validated @RequestBody ResetPasswordDto dto) {
        sysUserService.updateForgetPass(dto);
        return Result.success(null);
    }

    @Log(title = "??????excel",isSaveRequestData = false,isSaveResponseData = false)
    @ApiOperation("??????excel")
    @GetMapping("/download")
    @PreAuthorize("hasAuthority('sys:common:all')")
    public void downloadExcel(HttpServletResponse response){
        List<SysUser> list = sysUserService.getList();
        excelUtil.createExcel(list,SysUser.class,response);
    }

    @Log(title = "?????????????????????",isSaveRequestData = false,isSaveResponseData = false)
    @ApiOperation("?????????????????????")
    @GetMapping("/pass/confirm/{used}")
    @PreAuthorize("hasAuthority('sys:common:all')")
    public Result<Boolean> confirmOldPassword(@PathVariable("used") String used) {
        Long id = Long.parseLong((String) request.getAttribute(Const.USER_ID));
        Boolean b = sysUserService.confirmOldPassword(id,used);
        return Result.success(b);
    }

    @Log(title = "?????????????????????",isSaveRequestData = false,isSaveResponseData = false)
    @ApiOperation("?????????????????????")
    @GetMapping("/pass/re/{password}")
    @PreAuthorize("hasAuthority('sys:common:all')")
    public Result<Boolean> ResetPassword(@PathVariable("password") String password) {
        Long id = Long.parseLong((String) request.getAttribute(Const.USER_ID));
        Boolean b = sysUserService.updateNewPassword(id,password);
        return Result.success(b);
    }
}
