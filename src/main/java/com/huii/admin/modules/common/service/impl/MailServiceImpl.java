package com.huii.admin.modules.common.service.impl;

import com.huii.admin.common.utils.MailUtil;
import com.huii.admin.modules.common.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private MailUtil mailUtil;

    @Override
    public void sendResetPassCode(String email,String code) {
        String subject = "重置密码";
        String content = "code:"+code;
        try {
            mailUtil.sendHtmlMail(email,subject,content);
        } catch (MessagingException e) {
            throw new RuntimeException("邮件发送异常,请稍后重试");
        }
    }
}
