package edu.nuaa.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author brain
 * @version 1.0
 * @date 2023/5/6 15:36
 */
public class Producer {
    //消息队列名称
    public static final String QUEUE_NAME = "hello";

    //生产者发消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //连接到mq
        ConnectionFactory factory = new ConnectionFactory();//工厂  池化思想（线程池 对象池等等）
        //下面这些自然想到用配置文件
        factory.setHost("192.168.204.128");//Host
        factory.setUsername("zk");//username
        factory.setPassword("zk10230705");//password
        Connection connection = factory.newConnection();//从工厂 取连接
        //获取channel  这里是否可以猜想rabbitmq使用的IO模式是基于事件驱动的IO？后续看看源码
        Channel channel = connection.createChannel();
        //队列和交换机 这里只使用队列
        /**
         * Declare a queue
         * @param queue the name of the queue 队列名
         * @param durable 是否进行持久化
         * @param exclusive 是否只供一个消费者使用
         * @param autoDelete 没有消费者时是否自动删除消息
         * @param arguments other properties (construction arguments) for the queue
         * @return a declaration-confirm method to indicate the queue was successfully declared
         * @throws java.io.IOException if an error is encountered
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        String message = "hello,world";
        /**
         * Publish a message.
         * Publishing to a non-existent exchange will result in a channel-level protocol exception, which closes the channel.
         * Invocations of <code>Channel#basicPublish</code> will eventually block if a
         * <a href="https://www.rabbitmq.com/alarms.html">resource-driven alarm</a> is in effect.
         * @param exchange the exchange to publish the message to 交换机
         * @param routingKey the routing key 路由key
         * @param props other properties for the message - routing headers etc
         * @param body the message body 消息体
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送完毕");
    }
}
