package com.qf.temprabbitmqspringboots.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author maizifeng
 * @Date 2019/6/20
 */
@Configuration
public class RabbitMQConfig {
    //simple模式
    //创建队列
    @Bean
    public Queue getQueue(){
        return new Queue("simple_queue_springboot");
    }

    //fanout模式
    //创建队列
    @Bean
    public Queue getQueue1(){
        return new Queue("fanout_queue_springboot1");
    }

    @Bean
    public Queue getQueue2(){
        return new Queue("fanout_queue_springboot2");
    }

    //创建交换机
    @Bean
    public FanoutExchange getFanoutExchange(){
        return new FanoutExchange("fanout_exchange_springboot");
    }
    //队列1绑定交换机
    @Bean
    public Binding bindingExchange1(Queue getQueue1,FanoutExchange getFanoutExchange){
        return BindingBuilder.bind(getQueue1).to(getFanoutExchange);
    }
    //队列2绑定交换机
    @Bean
    public Binding bindingExchange2(Queue getQueue2,FanoutExchange getFanoutExchange){
        return BindingBuilder.bind(getQueue2).to(getFanoutExchange);
    }

    //topic模式
    //创建队列
    @Bean
    public Queue getTopicQueue1(){
        return new Queue("topic_queue_springboot");
    }

    //创建交换机
    @Bean
    public TopicExchange getTopicExchange(){
        return new TopicExchange("topic_exchange_springboot");
    }
    //队列1绑定交换机
    @Bean
    public Binding topicBindingExchange1(Queue getTopicQueue1,TopicExchange getTopicExchange){
        return BindingBuilder.bind(getTopicQueue1).to(getTopicExchange).with("nba.#");
    }

}
