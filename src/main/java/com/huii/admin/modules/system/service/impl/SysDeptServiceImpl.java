package com.huii.admin.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.admin.modules.system.entity.SysDept;
import com.huii.admin.modules.system.mapper.SysDeptMapper;
import com.huii.admin.modules.system.service.SysDeptService;
import org.springframework.stereotype.Service;

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {
}
