package com.qf.temprabbitmqspringboots.simple;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author maizifeng
 * @Date 2019/6/20
 */
@Component
@RabbitListener(queues = "simple_queue_springboot")
public class Consumer {
    @RabbitHandler
    public void getMsg(String msg){
        System.out.println(msg);
    }
    @RabbitHandler
    public void getMsg(Student stu){
        System.out.println(stu);
    }
}
