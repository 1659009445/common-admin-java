package com.huii.admin.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huii.admin.common.annotation.FiledName;
import com.huii.admin.common.annotation.NotField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("角色表")
@FiledName("角色")
@TableName("sys_role")
@EqualsAndHashCode(callSuper = true)
public class SysRole extends BaseEntity implements Serializable {

    @NotField
    private static final long serialVersionUID = 1L;

    @ApiParam("角色名称")
    @NotBlank(message = "角色名称不能为空!")
    private String name;

    @ApiParam("角色编码")
    private String code;

    @NotField
    @TableField(exist = false)
    private List<Long> menuIdList = new ArrayList<>();

}
