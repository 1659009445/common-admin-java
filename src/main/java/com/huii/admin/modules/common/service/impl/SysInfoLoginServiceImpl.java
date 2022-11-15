package com.huii.admin.modules.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.admin.modules.common.entity.SysInfoLogin;
import com.huii.admin.modules.common.mapper.SysInfoLoginMapper;
import com.huii.admin.modules.common.service.SysInfoLoginService;
import org.springframework.stereotype.Service;

@Service
public class SysInfoLoginServiceImpl extends ServiceImpl<SysInfoLoginMapper, SysInfoLogin> implements SysInfoLoginService {
}
