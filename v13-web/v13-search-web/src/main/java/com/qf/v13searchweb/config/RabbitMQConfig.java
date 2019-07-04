package com.qf.v13searchweb.config;

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
        return new Queue(RabbitMQConstant.PRODUCT_SEARCH_QUEUE, true, false, false);
    }

    @Bean
    public TopicExchange initTopicExchange(){
        return new TopicExchange(RabbitMQConstant.CENTER_PRODUCT_EXCHANGE);
    }

    @Bean
    public Binding initBinding(TopicExchange initTopicExchange,Queue initQueue){
        return BindingBuilder.bind(initQueue).to(initTopicExchange).with("search.add");
    }

}
