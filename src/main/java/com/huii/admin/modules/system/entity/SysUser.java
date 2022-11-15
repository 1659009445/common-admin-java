package com.huii.admin.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.huii.admin.common.annotation.FiledName;
import com.huii.admin.common.annotation.NotField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("用户表")
@FiledName("用户")
@TableName("sys_user")
public class SysUser implements Serializable{

    @NotField
    private static final long serialVersionUID = 1L;

    @ApiParam("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiParam("用户姓名")
    private String name;

    @ApiParam("用户名")
    @NotBlank(message = "用户名不能为空!")
    private String username;

    @ApiParam("密码")
    private String password;

    @ApiParam("邮箱")
    private String email;

    @ApiParam("电话")
    private String phone;

    @ApiParam("头像")
    private String avatar;

    @ApiParam("性别")
    private int sexual;

    @ApiParam("创建时间")
    private LocalDateTime createTime;

    @ApiParam("登录时间")
    private LocalDateTime lastLoginTime;

    @ApiParam("登录IP")
    private String lastLoginIp;

    @ApiParam("是否禁用")
    private int isBanned;

    @ApiParam("是否删除")
    @TableLogic
    private int isDeleted;

    @NotField
    @TableField(exist = false)
    private List<SysRole> roleList = new ArrayList<>();

}
