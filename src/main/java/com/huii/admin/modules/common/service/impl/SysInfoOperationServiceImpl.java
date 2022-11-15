package com.huii.admin.modules.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.admin.modules.common.entity.SysInfoOperation;
import com.huii.admin.modules.common.mapper.SysInfoOperationMapper;
import com.huii.admin.modules.common.service.SysInfoOperationService;
import org.springframework.stereotype.Service;

@Service
public class SysInfoOperationServiceImpl extends ServiceImpl<SysInfoOperationMapper, SysInfoOperation> implements SysInfoOperationService {
}
