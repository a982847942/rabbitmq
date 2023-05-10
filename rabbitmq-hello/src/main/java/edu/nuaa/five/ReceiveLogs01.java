package edu.nuaa.five;

import com.rabbitmq.client.Channel;
import edu.nuaa.util.RabbitMqUtils;

/**
 * @author brain
 * @version 1.0
 * @date 2023/5/8 14:12
 */
public class ReceiveLogs01 {
    public static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //声明队列，临时队列
        String queue = channel.queueDeclare().getQueue();
        //绑定交换机和队列
        channel.queueBind(queue,EXCHANGE_NAME,"");
        System.out.println("C1等待接收消息，把接收到的消息打印在屏幕上..........");
        channel.basicConsume(queue,true,((consumerTag, message) -> {
            System.out.println("C1接收到的消息:" + new String(message.getBody(),"utf-8"));
        }),(consumerTag -> {

        }));

    }
}
