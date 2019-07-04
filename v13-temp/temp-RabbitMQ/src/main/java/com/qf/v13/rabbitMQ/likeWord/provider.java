package com.qf.v13.rabbitMQ.likeWord;

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
    private static String exchangeName="topic_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("192.168.216.129");
        connectionFactory.setVirtualHost("/java1902");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName, "topic");

            String msg="asdf";
            String msg2="qwer";
            channel.basicPublish(exchangeName, "a.a", null, msg.getBytes());
            channel.basicPublish(exchangeName, "a.a.a", null, msg2.getBytes());

            channel.basicPublish(exchangeName, "b.b", null, msg.getBytes());
            channel.basicPublish(exchangeName, "b.b.b", null, msg2.getBytes());
        System.out.println("发送消息成功！");
    };
}
