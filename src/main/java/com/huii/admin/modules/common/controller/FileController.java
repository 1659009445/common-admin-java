package com.huii.admin.modules.common.controller;

import com.huii.admin.common.annotation.Log;
import com.huii.admin.common.lang.Const;
import com.huii.admin.common.result.Result;
import com.huii.admin.modules.common.service.FileService;
import com.huii.admin.oss.service.OssService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "file")
@RestController
@RequestMapping("file")
public class FileController {

	@Autowired
	private FileService fileService;

	@Autowired
	private OssService ossService;

	@Autowired
	private HttpServletRequest request;

	/**
	 * 简单文件上传
	 * @param multipartFile multipartFile
	 * @return null
	 */
	@Log(title = "文件上传",isSaveResponseData = false,isSaveRequestData = false)
	@RequestMapping("upload")
	public Result<?> uploadFile(@RequestParam("file") MultipartFile multipartFile){
		fileService.upload(multipartFile);

		return Result.success(null);
	}

	@Log(title = "头像上传",isSaveResponseData = false,isSaveRequestData = false)
	@RequestMapping("avatar")
	public Result<?> uploadAvatar(@RequestParam("file") MultipartFile multipartFile){
		Long id = Long.parseLong((String) request.getAttribute(Const.USER_ID));
		ossService.uploadAvatar(multipartFile,id);

		return Result.success(null);
	}

}
