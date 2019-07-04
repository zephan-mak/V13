package com.qf.v13commonservice.consumer;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.qf.v13.api.IEmailService;
import com.qf.v13.api.ISmsService;
import com.qf.v13.common.constant.RabbitMQConstant;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author maizifeng
 * @Date 2019/6/25
 */
@Component
public class Consumer {

    @Autowired
    private IEmailService emailService;

    @Autowired
    private ISmsService smsService;

    @RabbitHandler
    @RabbitListener(queues = RabbitMQConstant.EMAIL_QUEUE)
    public void process(Map<String,String> map){
        String to = map.get("to");
        String subject = map.get("subject");
        String text = map.get("text");

        emailService.send(to, subject, text);
    }
    @RabbitHandler
    @RabbitListener(queues = RabbitMQConstant.PHONE_QUEUE)
    public void getPhoneCode(String phone) throws ClientException {
        SendSmsResponse sendSmsResponse = smsService.sendSms(phone);
        String code = sendSmsResponse.getCode();
    }
}
