package com.qf.v13registerweb.config;

import com.qf.v13.common.constant.RabbitMQConstant;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author maizifeng
 * @Date 2019/6/25
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange init(){
        return new TopicExchange(RabbitMQConstant.REGISTER_EXCHANGE);
    }
}
