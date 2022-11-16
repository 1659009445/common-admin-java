package com.huii.admin.modules.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.admin.common.utils.PageUtil;
import com.huii.admin.common.utils.QueryUtil;
import com.huii.admin.common.utils.TimeUtil;
import com.huii.admin.modules.common.entity.SysInfoOperation;
import com.huii.admin.modules.common.mapper.SysInfoOperationMapper;
import com.huii.admin.modules.common.service.SysInfoOperationService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysInfoOperationServiceImpl extends ServiceImpl<SysInfoOperationMapper, SysInfoOperation> implements SysInfoOperationService {

	@Autowired
	TimeUtil timeUtil;

	@Autowired
	SysInfoOperationMapper sysInfoOperationMapper;

	@Override
	public PageUtil queryList(Map<String, Object> params) {
		QueryUtil<SysInfoOperation> queryUtil = new QueryUtil<>();
		IPage<SysInfoOperation> iPage = queryUtil.getPageInfo(params);

		//用户
		String user= (String) params.get("user");
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

		LambdaQueryWrapper<SysInfoOperation> wrapper = new LambdaQueryWrapper<>();
		wrapper.like(ObjectUtils.isNotEmpty(user),SysInfoOperation::getUserName,user)
				.like(ObjectUtils.isNotEmpty(result),SysInfoOperation::getOpInfo,result)
				.ge(ObjectUtils.isNotEmpty(begin),SysInfoOperation::getOpTime,begin)
				.le(ObjectUtils.isNotEmpty(end),SysInfoOperation::getOpTime,end)
				.orderByDesc(SysInfoOperation::getId)
				.select(SysInfoOperation::getId,
						SysInfoOperation::getUserName,
						SysInfoOperation::getOpIp,
						SysInfoOperation::getOpTime,
						SysInfoOperation::getOpMethod,
						SysInfoOperation::getOpTitle,
						SysInfoOperation::getReqMethod,
						SysInfoOperation::getOpInfo,
						SysInfoOperation::getErrInfo);

		IPage<SysInfoOperation> page = this.page(iPage,wrapper);

		return new PageUtil(page);
	}

	@Override
	public List<SysInfoOperation> getList() {
		return sysInfoOperationMapper.selectList(null);
	}
}
