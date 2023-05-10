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
        /**
         *  根据处理能力负载均衡
         *  不设置basicQos的话是一次性平均分发给所有的队列。
         *  设置之后限制了一次分发消息的数量，再设置手动确认机制，
         *  这样当你还没提交已经处理好的时候他是不会给你消息的，这样才能实现不公平分发。
         */

        channel.basicQos(1);
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
