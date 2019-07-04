package com.qf.v13commonservice.config;

import com.qf.v13.common.constant.RabbitMQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author maizifeng
 * @Date 2019/6/21
 */
@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue initQueue(){
        return new Queue(RabbitMQConstant.EMAIL_QUEUE, true, false, false);
    }

    @Bean
    public Queue initQueue2(){
        return new Queue(RabbitMQConstant.PHONE_QUEUE, true, false, false);
    }

    @Bean
    public TopicExchange initTopicExchange(){
        return new TopicExchange(RabbitMQConstant.REGISTER_EXCHANGE);
    }

    @Bean
    public Binding initBinding(TopicExchange initTopicExchange,Queue initQueue){
        return BindingBuilder.bind(initQueue).to(initTopicExchange).with("user.add");
    }

    @Bean
    public Binding initBinding2(TopicExchange initTopicExchange,Queue initQueue2){
        return BindingBuilder.bind(initQueue2).to(initTopicExchange).with("phoneCode.get");
    }
}
