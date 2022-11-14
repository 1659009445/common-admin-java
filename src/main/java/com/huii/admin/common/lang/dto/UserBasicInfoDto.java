package com.huii.admin.common.lang.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("用户基本信息DTO")
public class UserBasicInfoDto implements Serializable{

    private Long id;

    private String name;

    private String username;

    private String email;

    private String phone;

    private String avatar;

    private int sexual;

    private LocalDateTime createTime;

    private LocalDateTime lastLoginTime;

    private String lastLoginIp;

}
