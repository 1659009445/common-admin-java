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
@ApiModel("登录日志")
@FiledName("登录日志")
@TableName("sys_info_login")
public class SysInfoLogin implements Serializable {

	@NotField
	private static final long serialVersionUID = 1L;

	@ApiParam("ID")
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	@ApiParam("用户姓名")
	private String userName;

	@ApiParam("登陆时间")
	private String loginTime;

	@ApiParam("登录IP")
	private String loginIp;

	@ApiParam("登陆地点")
	private String loginPlace;

	@ApiParam("操作系统")
	private String loginSystem;

	@ApiParam("登录信息")
	private String loginInfo;

	@ApiParam("浏览器")
	private String loginBrowser;

}
