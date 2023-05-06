package edu.nuaa.two;

import com.rabbitmq.client.Channel;
import edu.nuaa.util.RabbitMqUtils;

import java.util.Scanner;

/**
 * @author brain
 * @version 1.0
 * @date 2023/5/6 17:03
 */
public class Task01 {
    public static final String QUEUE_NAME = "hello";

    //发送消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            channel.basicPublish("",QUEUE_NAME,null,scanner.next().getBytes());
        }
    }
}
