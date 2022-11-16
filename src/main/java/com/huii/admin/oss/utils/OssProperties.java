package com.huii.admin.oss.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "oss.config")
public class OssProperties implements InitializingBean {

	public static String END_POINT;
	public static String ACCESS_KEY_ID;
	public static String ACCESS_KEY_SECRET;
	public static String BUCKET_NAME;
	public static Boolean OPEN;

	@Value("${oss.config.end-point}")
	private String endpoint;

	@Value("${oss.config.key-id}")
	private String keyId;

	@Value("${oss.config.key-secret}")
	private String keySecret;

	@Value("${oss.config.bucket-name}")
	private String bucketName;

	@Value("${oss.config.open}")
	private Boolean open;


	@Override
	public void afterPropertiesSet() throws Exception {
		END_POINT=endpoint;
		ACCESS_KEY_ID=keyId;
		ACCESS_KEY_SECRET=keySecret;
		BUCKET_NAME=bucketName;
		OPEN = open;
	}
}
