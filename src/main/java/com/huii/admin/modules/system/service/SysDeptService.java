package com.huii.admin.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huii.admin.modules.system.entity.SysDept;

import java.util.List;

public interface SysDeptService extends IService<SysDept> {
	List<SysDept> getTreeList();

	Boolean deleteSysDeptR(Long id);
}
