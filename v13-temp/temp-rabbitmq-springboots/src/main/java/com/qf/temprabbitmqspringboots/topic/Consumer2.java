package com.qf.temprabbitmqspringboots.topic;

import com.qf.temprabbitmqspringboots.simple.Student;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author maizifeng
 * @Date 2019/6/20
 */
@Component
public class Consumer2 {


    @RabbitHandler
    @RabbitListener(queues = "topic_queue_springboot")
    public void topic1(Student student){
        System.out.println("1----"+student);
    }


}
