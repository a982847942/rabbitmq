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
 * 1.队列长度达到最大长度
 * 2.TTL过期
 * 3.消息被拒绝
 */
public class Consumer01 {
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
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        //模拟死信队列
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", "lisi");
//        arguments.put("x-max-length",6);//设置队列的最大长度（模拟死信队列）
//        arguments.put("x-message-ttl",10*1000 );//ttl

        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");


        System.out.println("Consumer等待接收消息....");
        channel.basicConsume(NORMAL_QUEUE, false, (((consumerTag, message) -> {
            System.out.println("Consumer01接收的消息是:" + new String(message.getBody(), "utf-8"));
        })), (consumerTag -> {

        }));
    }
}
