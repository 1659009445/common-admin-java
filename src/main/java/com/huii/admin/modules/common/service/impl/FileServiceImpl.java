package com.huii.admin.modules.common.service.impl;

import com.huii.admin.common.utils.FileUtil;
import com.huii.admin.modules.common.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

	@Autowired
	private FileUtil fileUtil;

	@Override
	public void upload(MultipartFile multipartFile) {
		fileUtil.upload(multipartFile);
	}
}
