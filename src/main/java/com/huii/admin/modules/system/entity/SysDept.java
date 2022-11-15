package com.huii.admin.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.huii.admin.common.annotation.FiledName;
import com.huii.admin.common.annotation.NotField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel("部门表")
@FiledName("部门")
@TableName("sys_dept")
@EqualsAndHashCode(callSuper = true)
public class SysDept extends BaseEntity implements Serializable {

	@NotField
	private static final long serialVersionUID = 1L;

	@ApiParam("部门名称")
	@NotBlank(message = "部门名称不能为空!")
	private String name;

	@ApiParam("部门编码")
	private String code;
}
