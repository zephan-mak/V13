package com.qf.tempsms.SmsTest;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;

/**
 * @author maizifeng
 * @Date 2019/6/26
 */
public class main {
    public static void main(String[] args) throws ClientException {
        String code = Integer.toString((int)(Math.random()*(9999-1000))+1000);
        System.out.println("发送的验证码为："+code);
        //发短信
        SendSmsResponse response =AliyunSmsUtils.sendSms("13537568428",code);
        System.out.println("短信接口返回的数据----------------");
        System.out.println("Code=" + response.getCode());
        System.out.println("Message=" + response.getMessage());
        System.out.println("RequestId=" + response.getRequestId());
        System.out.println("BizId=" + response.getBizId());
    };
}
