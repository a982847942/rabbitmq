package edu.nuaa.three;

import com.rabbitmq.client.Channel;
import edu.nuaa.util.RabbitMqUtils;
import edu.nuaa.util.SleepUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author brain
 * @version 1.0
 * @date 2023/5/6 19:50
 */
public class Work03 {
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        boolean autoAck = false;
        System.out.println("C1等待接受消息处理");
        channel.basicConsume(QUEUE_NAME,autoAck,(consumerTag, message)->{
            SleepUtils.sleep(1);
            System.out.println("接收到的消息:" + new String(message.getBody(),"utf-8"));
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        },(consumerTag)->{
            System.out.println("消费者取消消费接口回调逻辑");
        });
    }
}
