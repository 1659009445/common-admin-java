package com.huii.admin.modules.common.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	/**
	 * 文件上传
	 * @param multipartFile file
	 */
	void upload(MultipartFile multipartFile);

}
