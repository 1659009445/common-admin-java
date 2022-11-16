package com.huii.admin.modules.common.service;

public interface MailService {

	void sendResetPassCode(String email,String code);
}
