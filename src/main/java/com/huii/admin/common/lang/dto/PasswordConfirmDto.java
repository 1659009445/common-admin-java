package com.huii.admin.common.lang.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PasswordConfirmDto {

	@NotBlank(message = "旧密码不能为空")
	private String used;
}
