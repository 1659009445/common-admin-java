package com.huii.admin.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.huii.admin.modules.system.entity.SysUser;
import com.huii.admin.modules.system.mapper.SysUserMapper;
import com.huii.admin.oss.service.OssService;
import com.huii.admin.oss.utils.OssProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {


	@Autowired
	private SysUserMapper sysUserMapper;

	@Override
	public void uploadAvatar(MultipartFile multipartFile, Long userId) {
		String endPoint = OssProperties.END_POINT;
		String accessKeyId = OssProperties.ACCESS_KEY_ID;
		String accessKeySecret = OssProperties.ACCESS_KEY_SECRET;
		String bucketName = OssProperties.BUCKET_NAME;
		Boolean open = OssProperties.OPEN;
		if(open){
			try {
				//保存至oss
				OSS client = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
				InputStream inputStream = multipartFile.getInputStream();
				String fileName = UUID.randomUUID().toString().replaceAll("-","");
				String date = new DateTime().toString("yyyyMMdd");
				fileName = date + fileName;
				client.putObject(bucketName,fileName,inputStream);
				client.shutdown();
				String url="https://"+bucketName+"."+endPoint+"/"+fileName;
				System.out.println("URL"+url);
				//更新数据库
				SysUser sysUser = sysUserMapper.selectById(userId);
				sysUser.setAvatar(url);
				sysUserMapper.updateById(sysUser);
			} catch (IOException e) {
				throw new RuntimeException("该服务已关闭");
			}
		} else {
			throw new RuntimeException("该服务已关闭");
		}
	}
}
