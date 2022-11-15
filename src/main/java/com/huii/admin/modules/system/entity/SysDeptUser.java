package com.huii.admin.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("用户部门关联表")
@TableName("sys_dept_user")
public class SysDeptUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiParam("ID")
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	@ApiParam("部门ID")
	private Long deptId;

	@ApiParam("用户ID")
	private Long userId;
}