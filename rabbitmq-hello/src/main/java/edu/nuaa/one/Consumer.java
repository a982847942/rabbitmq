package edu.nuaa.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author brain
 * @version 1.0
 * @date 2023/5/6 15:58
 */
public class Consumer {
    //队列名
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.204.128");//Host
        factory.setUsername("zk");//username
        factory.setPassword("zk10230705");//password
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //消费消息
        channel.basicConsume(QUEUE_NAME,true,(consumerTag, message)->{
            //接收到消息
            System.out.println("接收到的消息:" + message.toString());
            System.out.println("消息体:" + new String(message.getBody()));
        },(consumerTag)->{
            //取消消费
            System.out.println("消息消费被中断");
        });
    }
}
