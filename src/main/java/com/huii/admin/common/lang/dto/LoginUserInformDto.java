package com.huii.admin.common.lang.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("登录成功返回信息")
public class LoginUserInformDto implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("username")
    private String username;

    @ApiModelProperty("最后登录ID")
    private String LastLoginIP;

    @ApiModelProperty("最后登录时间")
    private LocalDateTime LastLoginTime;
}
