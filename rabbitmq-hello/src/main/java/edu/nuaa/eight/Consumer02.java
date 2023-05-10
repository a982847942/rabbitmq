package edu.nuaa.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import edu.nuaa.util.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author brain
 * @version 1.0
 * @date 2023/5/9 9:25
 * 死信队列（消息由于某种原因无法消费进入死信队列等待处理）
 */
public class Consumer02 {
    //普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
    //普通队列名称
    public static final String NORMAL_QUEUE = "normal_queue";
    //死信队列名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("Consumer02等待接收消息....");
        channel.basicConsume(DEAD_QUEUE, true, (((consumerTag, message) -> {
            System.out.println("Consumer01接收的消息是:" + new String(message.getBody(), "utf-8"));
        })), (consumerTag -> {

        }));
    }
}
