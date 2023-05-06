package edu.nuaa.two;

import com.rabbitmq.client.Channel;
import edu.nuaa.util.RabbitMqUtils;

/**
 * @author brain
 * @version 1.0
 * @date 2023/5/6 16:51
 */
public class Worker01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("consumer2等待接受消息...");
        channel.basicConsume(QUEUE_NAME,true,(consumerTag, message)->{
            System.out.println("接收到的消息:" + new String(message.getBody()));
        },(consumerTag)->{
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        });
    }
}
