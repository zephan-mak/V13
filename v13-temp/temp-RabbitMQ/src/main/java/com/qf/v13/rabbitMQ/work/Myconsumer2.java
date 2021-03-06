package com.qf.v13.rabbitMQ.work;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author maizifeng
 * @Date 2019/6/20
 */
public class Myconsumer2 {
    private static String queueName="work_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("192.168.216.129");
        connectionFactory.setVirtualHost("/java1902");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //创建交互通道
        Channel channel = connection.createChannel();
        //监听队列，获取消息
        Consumer consumer=new DefaultConsumer(channel){
            //自动回调接受消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg=new String(body);
                System.out.println("接收到消息："+msg);
            }
        };
        //监听队列
        //auto自动回复
        channel.basicConsume(queueName, true, consumer);
    };
}
