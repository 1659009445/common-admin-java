package com.huii.admin.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huii.admin.common.annotation.Log;
import com.huii.admin.common.exception.NormalException;
import com.huii.admin.common.result.Result;
import com.huii.admin.modules.system.entity.SysDept;
import com.huii.admin.modules.system.service.SysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Api(tags = "dept")
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController {

	@Autowired
	private SysDeptService sysDeptService;

	@Log(title = "查询部门信息",isSaveRequestData = false,isSaveResponseData = false)
	@ApiOperation("查询单例")
	@GetMapping("/info/{id}")
	@PreAuthorize("hasAuthority('sys:dept:query')")
	public Result<SysDept> getOne(@PathVariable(name = "id") Long id){
		return Result.success(sysDeptService.getById(id));
	}

	@Log(title = "查询部门列表",isSaveRequestData = false,isSaveResponseData = false)
	@ApiOperation("查询全部")
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('sys:dept:query')")
	public Result<List<SysDept>> getList(){
		return Result.success(sysDeptService.getTreeList());
	}

	@Log(title = "添加部门信息")
	@ApiOperation("保存")
	@PostMapping("/insert")
	@PreAuthorize("hasAuthority('sys:dept:insert')")
	public Result<SysDept> insert(@Validated @RequestBody SysDept sysDept){
		sysDept.setCreated(LocalDateTime.now());
		sysDept.setUpdated(LocalDateTime.now());
		sysDeptService.save(sysDept);

		return Result.success(sysDept);
	}

	@Log(title = "更新部门信息")
	@ApiOperation("更新")
	@PostMapping("/update")
	@PreAuthorize("hasAuthority('sys:dept:update')")
	public Result<SysDept> update(@Validated @RequestBody SysDept sysDept){
		sysDept.setUpdated(LocalDateTime.now());
		sysDeptService.updateById(sysDept);

		return Result.success(sysDept);
	}

	@Log(title = "删除部门信息")
	@ApiOperation("删除")
	@PostMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('sys:dept:delete')")
	public Result<Object> delete(@PathVariable("id") Long id){
		int count = (int) sysDeptService.count(new LambdaQueryWrapper<SysDept>().eq(SysDept::getPid, id));
		if (count > 0) {
			throw new NormalException("请先删除子部门!");
		}

		sysDeptService.removeById(id);
		sysDeptService.deleteSysDeptR(id);

		return Result.success(null);
	}

}
