package com.qf.temprabbitmqspringboots;

import com.qf.temprabbitmqspringboots.simple.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TempRabbitmqSpringbootsApplicationTests {

    @Autowired
    private RabbitTemplate template;

    @Test
    public void contextLoads() {
        template.convertAndSend("simple_queue_springboot", "整合成功");
    }

    @Test
    public void contextLoads2() {
        Student student=new Student(1, "aa");
        template.convertAndSend("simple_queue_springboot", student);
    }

    @Test
    public void publishTest() {
        for (int i = 1; i <= 10; i++) {
            Student student=new Student(i, "aa"+i);
            template.convertAndSend("fanout_exchange_springboot", "", student);
        }
    }

    @Test
    public void topicTest() {
        Student student=new Student(1, "nba");
        Student student2=new Student(2, "cba");
        template.convertAndSend("topic_exchange_springboot", "nba.q.q", student);
        template.convertAndSend("topic_exchange_springboot", "nba.q", student2);
    }
}
