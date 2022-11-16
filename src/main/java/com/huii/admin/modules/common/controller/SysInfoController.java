package com.huii.admin.modules.common.controller;

import com.huii.admin.common.annotation.Log;
import com.huii.admin.common.result.Result;
import com.huii.admin.common.utils.ExcelUtil;
import com.huii.admin.common.utils.PageUtil;
import com.huii.admin.modules.common.entity.SysInfoOperation;
import com.huii.admin.modules.common.service.SysInfoLoginService;
import com.huii.admin.modules.common.service.SysInfoOperationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Api(tags = "日志")
@RestController
@RequestMapping("info")
public class SysInfoController {

	@Autowired
	private SysInfoOperationService sysInfoOperationService;

	@Autowired
	private SysInfoLoginService sysInfoLoginService;

	@Autowired
	private ExcelUtil excelUtil;

	@Log(title = "获取日志列表",isSaveRequestData = false,isSaveResponseData = false)
	@ApiOperation("查询日志")
	@GetMapping("op/list")
	@PreAuthorize("hasAuthority('sys:manage')")
	public Result<PageUtil> getList(@RequestParam Map<String, Object> params) {
		PageUtil page = sysInfoOperationService.queryList(params);
		return Result.success(page);
	}

	@Log(title = "删除日志列表")
	@ApiOperation("删除日志")
	@GetMapping("op/delete")
	@PreAuthorize("hasAuthority('sys:manage')")
	public Result<Object> delete(@RequestBody Long[] ids) {
		sysInfoOperationService.removeBatchByIds(Arrays.asList(ids));
		return Result.success(null);
	}

	@Log(title = "导出excel",isSaveRequestData = false,isSaveResponseData = false)
	@ApiOperation("导出excel")
	@GetMapping("op/download")
	@PreAuthorize("hasAuthority('sys:manage')")
	public void downloadExcel(HttpServletResponse response){
		List<SysInfoOperation> list = sysInfoOperationService.getList();
		excelUtil.createExcel(list,SysInfoOperation.class,response);
	}

}
