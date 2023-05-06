package edu.nuaa.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author brain
 * @version 1.0
 * @date 2023/5/6 16:46
 */
public class RabbitMqUtils {
    private static ConnectionFactory factory;

    static {
        factory = new ConnectionFactory();
        factory.setHost("192.168.204.128");//Host
        factory.setUsername("zk");//username
        factory.setPassword("zk10230705");//password
    }

    public static Channel getChannel() throws Exception {

        Connection connection = factory.newConnection();//从工厂 取连接
        Channel channel = connection.createChannel();//获取channel
        return channel;
    }
}
