package com.qf.temprabbitmqspringboots.publish;

import com.qf.temprabbitmqspringboots.simple.Student;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author maizifeng
 * @Date 2019/6/20
 */
@Component
public class Consumer1 {

    @RabbitHandler
    @RabbitListener(queues = "fanout_queue_springboot1")
    public void Consumer1(Student student) {
        System.out.println("1----"+student);
    }

    @RabbitHandler
    @RabbitListener(queues = "fanout_queue_springboot2")
    public void Consumer2(Student student) {
        System.out.println("2----"+student);
    }


}
