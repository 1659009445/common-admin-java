package com.huii.admin.modules.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huii.admin.common.utils.PageUtil;
import com.huii.admin.modules.common.entity.SysInfoLogin;

import java.util.List;
import java.util.Map;

public interface SysInfoLoginService extends IService<SysInfoLogin> {
	List<SysInfoLogin> getList();

	PageUtil queryList(Map<String, Object> params);
}
