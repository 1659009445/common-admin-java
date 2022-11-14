package com.huii.admin.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("菜单表")
@TableName("sys_menu")
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiParam("菜单名称")
    @NotBlank(message = "菜单名称不能为空!")
    private String name;

    @ApiParam("菜单编码")
    private String code;

    @ApiParam("父ID")
    private Long pid;

    @ApiParam("组件")
    private String component;

    @ApiParam("图标")
    private String icon;

    @ApiParam("路径")
    private String url;

    @ApiParam("权限")
    private String perms;

    @ApiParam("是否展示在菜单中")
    @TableField("is_title")
    private int isTitle;

    @ApiParam("展示在菜单中的排序")
    @TableField("orderNum")
    private Integer orderNum;

    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();

}
