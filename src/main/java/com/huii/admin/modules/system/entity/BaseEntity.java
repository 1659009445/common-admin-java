package com.huii.admin.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("BaseEntity")
public class BaseEntity implements Serializable {

    @ApiParam("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiParam("创建时间")
    private LocalDateTime created;

    @ApiParam("更新时间")
    private LocalDateTime updated;

    @ApiParam("描述")
    private String inform;

    @ApiParam("状态")
    private int status;
}
