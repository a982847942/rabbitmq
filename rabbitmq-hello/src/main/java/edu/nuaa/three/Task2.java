package edu.nuaa.three;

import com.rabbitmq.client.Channel;
import edu.nuaa.util.RabbitMqUtils;

import java.util.Scanner;

/**
 * @author brain
 * @version 1.0
 * @date 2023/5/6 19:44
 *消息在手动应答时不丢失，出现丢失确认时会放回队列中重新消费
 */
public class Task2 {
    //队列名称
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()){
            String message = sc.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes("utf-8"));
            System.out.println("生产者发送消息："  + message);
        }
    }
}
