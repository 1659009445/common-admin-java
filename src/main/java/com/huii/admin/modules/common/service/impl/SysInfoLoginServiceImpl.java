package com.huii.admin.modules.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.admin.common.utils.PageUtil;
import com.huii.admin.common.utils.QueryUtil;
import com.huii.admin.common.utils.TimeUtil;
import com.huii.admin.modules.common.entity.SysInfoLogin;
import com.huii.admin.modules.common.mapper.SysInfoLoginMapper;
import com.huii.admin.modules.common.service.SysInfoLoginService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysInfoLoginServiceImpl extends ServiceImpl<SysInfoLoginMapper, SysInfoLogin> implements SysInfoLoginService {

	@Autowired
	TimeUtil timeUtil;

	@Autowired
	private SysInfoLoginMapper sysInfoLoginMapper;

	@Override
	public List<SysInfoLogin> getList() {
		return sysInfoLoginMapper.selectList(null);
	}

	@Override
	public PageUtil queryList(Map<String, Object> params) {
		QueryUtil<SysInfoLogin> queryUtil = new QueryUtil<>();
		IPage<SysInfoLogin> iPage = queryUtil.getPageInfo(params);
		//操作结果
		String place = (String) params.get("place");
		//操作结果
		String result = (String) params.get("result");
		//起止时间
		String time = (String) params.get("time");
		String begin = null;
		String end = null;
		if(StringUtils.isNotEmpty(time) && ObjectUtils.isNotEmpty(time)){
			end = timeUtil.castGMTTime(time.substring(','-1));
			begin = timeUtil.castGMTTime(time.substring(0,','-2));
		}
		LambdaQueryWrapper<SysInfoLogin> wrapper = new LambdaQueryWrapper<>();
		wrapper.like(ObjectUtils.isNotEmpty(place),SysInfoLogin::getLoginPlace,place)
				.like(ObjectUtils.isNotEmpty(result),SysInfoLogin::getLoginInfo,result)
				.ge(ObjectUtils.isNotEmpty(begin),SysInfoLogin::getLoginTime,begin)
				.le(ObjectUtils.isNotEmpty(end),SysInfoLogin::getLoginTime,end)
				.orderByDesc(SysInfoLogin::getId);

		IPage<SysInfoLogin> page = this.page(iPage, wrapper);

		return new PageUtil(page);
	}
}
