package com.huii.admin.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("角色菜单关联表")
@TableName("sys_role_menu")
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiParam("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiParam("角色ID")
    private Long roleId;

    @ApiParam("菜单ID")
    private Long menuId;
}
