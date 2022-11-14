package com.huii.admin.common.lang.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel("修改密码DTO")
public class ResetPasswordDto implements Serializable {

	@NotBlank(message = "用户名不能为空")
	private String username;

	@NotBlank(message = "邮箱不能为空")
	private String email;

	@NotBlank(message = "旧密码不能为空")
	private String password;

	@NotBlank(message = "新密码不能为空")
	private String confirm;

	@NotBlank(message = "验证码不为空")
	private String code;

	@ApiModelProperty("UUID")
	private String key;
}
