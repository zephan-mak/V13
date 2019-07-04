package com.qf.tempemail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TempEmailApplicationTests {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${mail.fromAdd}")
    private String fromAdd;

    @Value("${mail.toAdd}")
    private String toAdd;

    @Test
    public void contextLoads() {
        SimpleMailMessage mail=new SimpleMailMessage();
        mail.setTo(toAdd);
        mail.setFrom(fromAdd);
        mail.setSubject("主题");
        mail.setText("test");
        javaMailSender.send(mail);
        System.out.println("发送邮件成功");
    }

    @Test
        public void htmlTest() {
            String context="<a href='http://localhost:9090/'>地址</a>";
            MimeMessage mailMessage=javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper messageHelper=new MimeMessageHelper(mailMessage, true);
                messageHelper.setTo(fromAdd);
                messageHelper.setFrom(fromAdd);
                messageHelper.setSubject("激活连接");
                messageHelper.setText(context,true);
                javaMailSender.send(mailMessage);
                System.out.println("发送邮件成功");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
    }

    @Test
    public void tempTest() {
        MimeMessage mailMessage=javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper=new MimeMessageHelper(mailMessage, true);
            messageHelper.setTo(fromAdd);
            messageHelper.setFrom(fromAdd);
            messageHelper.setSubject("激活连接");

            Context context=new Context();
            context.setVariable("username", "aa");
            String text= templateEngine.process("test.html",context);
            messageHelper.setText(text,true);
            javaMailSender.send(mailMessage);
            System.out.println("发送邮件成功");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
