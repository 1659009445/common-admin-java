package com.huii.admin.modules.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huii.admin.common.annotation.FiledName;
import com.huii.admin.common.annotation.NotField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("操作日志")
@FiledName("操作日志")
@TableName("sys_info_operation")
public class SysInfoOperation implements Serializable {

	@NotField
	private static final long serialVersionUID = 1L;

	@ApiParam("ID")
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	@ApiParam("用户姓名")
	private String userName;

	@ApiParam("操作时间")
	private String opTime;

	@ApiParam("操作IP")
	private String opIp;

	@ApiParam("操作方法")
	private String opMethod;


	@ApiParam("操作信息")
	private String opInfo;
}
