package com.qf.v13commonservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v13.api.IEmailService;
import com.qf.v13.common.pojo.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author maizifeng
 * @Date 2019/6/25
 */
@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${mail.fromAdd}")
    private String fromAdd;
    @Override
    public ResultBean send(String to, String subject, String text) {
        MimeMessage mailMessage=javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper=new MimeMessageHelper(mailMessage, true);
            messageHelper.setTo(to);
            messageHelper.setFrom(fromAdd);
            messageHelper.setSubject(subject);
            messageHelper.setText(text,true);
            javaMailSender.send(mailMessage);
            System.out.println("发送邮件成功");
            return new ResultBean("200", "发送邮件成功");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
