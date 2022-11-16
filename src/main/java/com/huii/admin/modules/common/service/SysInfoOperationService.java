package com.huii.admin.modules.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huii.admin.common.utils.PageUtil;
import com.huii.admin.modules.common.entity.SysInfoOperation;

import java.util.List;
import java.util.Map;

public interface SysInfoOperationService extends IService<SysInfoOperation> {
	PageUtil queryList(Map<String, Object> params);

	List<SysInfoOperation> getList();
}
