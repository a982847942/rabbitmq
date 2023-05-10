package edu.nuaa.five;

import com.rabbitmq.client.Channel;
import edu.nuaa.util.RabbitMqUtils;

import java.util.Scanner;

/**
 * @author brain
 * @version 1.0
 * @date 2023/5/8 14:25
 */
public class EmitLog {
    public static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()){
            String message = sc.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes("utf-8"));
            System.out.println("生产者发送消息:" + message);
        }
    }
}
