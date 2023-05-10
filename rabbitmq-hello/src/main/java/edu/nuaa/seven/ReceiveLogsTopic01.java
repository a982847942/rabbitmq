package edu.nuaa.seven;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import edu.nuaa.util.RabbitMqUtils;

/**
 * @author brain
 * @version 1.0
 * @date 2023/5/8 21:02
 * Topic交换机
 */
public class ReceiveLogsTopic01 {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName = "Q1";
        channel.queueDeclare(queueName,false,false,false,null);
        channel.queueBind(queueName,EXCHANGE_NAME,"*.orange.*");
        System.out.println("等待接收消息...");
        channel.basicConsume(queueName,true,((consumerTag, message) -> {
            System.out.println("C1收到消息:" + new String(message.getBody(),"utf-8"));
            System.out.println("接收队列:" + queueName + " 绑定键:" + message.getEnvelope().getRoutingKey());
        }),(consumerTag -> {

        }));
    }
}
