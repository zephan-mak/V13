package com.qf.v13.rabbitMQ.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author maizifeng
 * @Date 2019/6/20
 */
public class provider {
    private static String queueName="simple_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("192.168.216.129");
        connectionFactory.setVirtualHost("/java1902");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, false, false, false, null);

        String msg="啊啊啊";
        channel.basicPublish("", queueName, null, msg.getBytes());
        System.out.println("发送消息成功！");
    };
}
