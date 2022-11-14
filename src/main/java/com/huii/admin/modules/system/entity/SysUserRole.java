package com.huii.admin.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("用户角色关联表")
@TableName("sys_user_role")
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiParam("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiParam("用户ID")
    private Long userId;

    @ApiParam("角色ID")
    private Long roleId;
}
