package com.qf.v13payweb.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author maizifeng
 * @Date 2019/7/2
 */
@Configuration
public class PayConfig {

    @Value("${AliPay.serverUrl}")
    private String serverUrl;

    @Value("${AliPay.privateKe}")
    private String privateKe;

    @Value("${AliPay.aliPayPublicKey}")
    private String aliPayPublicKey;

    @Bean
    public AlipayClient getAliPayClient(){
        AlipayClient alipayClient = new DefaultAlipayClient(serverUrl,
                "2016101000650773", privateKe, "json", "utf-8", aliPayPublicKey, "RSA2"); //获得初始化的AliPayClient
        return alipayClient;
    }
}
