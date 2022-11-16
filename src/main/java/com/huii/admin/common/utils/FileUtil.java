package com.huii.admin.common.utils;


import com.huii.admin.common.exception.NormalException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class FileUtil {

	@Value("${file.path}")
	private String PATH;

	@Value("#{T(java.lang.Long).parseLong('${file.max:50}')}")
	private Long MAX_SIZE;

	static final String[] DEFAULT_EXTENSION = {
		"rar", "zip", "gz", "bz2", "7z",
		"gif", "jpg", "jpeg", "png", "bpm",
		"avi", "mp3", "mp4", "WMV",
		"pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
		"html", "htm" , "txt"
	};

	static String getRandomName() {
		String randomUUID = UUID.randomUUID().toString();
		String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		return time+ randomUUID.substring(0,6);
	}

	/**
	 * 简单文件上传
	 * @param multipartFile multipartFile
	 */
	public void upload(MultipartFile multipartFile){
		//获取文件名并判断文件类型是否合法
		String filename = multipartFile.getOriginalFilename();
		assert filename != null;
		String extension = filename.substring(filename.lastIndexOf(".")+1);
		boolean flag = false;
		for (String s : DEFAULT_EXTENSION){
			if (s.equals(extension)) {
				flag = true;
				break;
			}
		}
		if(flag) {
			if(multipartFile.getSize() <= MAX_SIZE * 1024 * 1024){
				System.out.println(1);
				String __filename = getRandomName()+ "." +extension;
				File file = new File(PATH+__filename);
				try {
					multipartFile.transferTo(file);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}else {
				throw new NormalException("500","文件大小超出限制!");
			}
		} else {
			throw new NormalException("500","文件不合法!");
		}

	}

	/**
	 * 文件上传至oss
	 * @param multipartFile
	 * @param fileName
	 */
	public void uploadOss(MultipartFile multipartFile, String fileName){

	}

}
