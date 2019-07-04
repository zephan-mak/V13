package com.qf.v13.api;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;

/**
 * @author maizifeng
 * @Date 2019/6/25
 */
public interface ISmsService {
    public SendSmsResponse sendSms(String telephone) throws ClientException;
}
