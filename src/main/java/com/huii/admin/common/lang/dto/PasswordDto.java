package com.huii.admin.common.lang.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel("修改密码DTO")
public class PasswordDto implements Serializable {

    @NotBlank(message = "旧密码不能为空")
    private String oldPass;

    @NotBlank(message = "新密码不能为空")
    private String newPass;

}