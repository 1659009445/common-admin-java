package com.huii.admin.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {

	/**
	 * 用户上传头像
	 * @param multipartFile
	 * @param userId
	 */
	void uploadAvatar(MultipartFile multipartFile, Long userId);
}
