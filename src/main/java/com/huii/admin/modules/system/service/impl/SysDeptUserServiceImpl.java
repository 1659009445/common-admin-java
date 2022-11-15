package com.huii.admin.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.admin.modules.system.entity.SysDeptUser;
import com.huii.admin.modules.system.mapper.SysDeptUserMapper;
import com.huii.admin.modules.system.service.SysDeptUserService;
import org.springframework.stereotype.Service;

@Service
public class SysDeptUserServiceImpl extends ServiceImpl<SysDeptUserMapper, SysDeptUser> implements SysDeptUserService {
}
