package com.huii.admin.modules.common.controller;

import com.huii.admin.common.result.Result;
import com.huii.admin.modules.common.service.FileService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "file")
@RestController
@RequestMapping("file")
public class FileController {

	@Autowired
	private FileService fileService;

	/**
	 * 简单文件上传
	 * @param multipartFile multipartFile
	 * @return null
	 */
	@RequestMapping("upload")
	public Result<?> upload(@RequestParam("file") MultipartFile multipartFile){
		fileService.upload(multipartFile);

		return Result.success(null);
	}

	//TODO 头像

}
