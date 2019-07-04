package com.qf.v13.rabbitMQ.publish;

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
    private static String exchangeName="fanout_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("192.168.216.129");
        connectionFactory.setVirtualHost("/java1902");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName, "fanout");

        for (int i = 1; i <= 10; i++) {
            String msg="第"+i+"次";
            channel.basicPublish(exchangeName, "", null, msg.getBytes());
        }
        System.out.println("发送消息成功！");
    };
}
