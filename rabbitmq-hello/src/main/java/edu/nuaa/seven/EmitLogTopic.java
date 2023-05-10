package edu.nuaa.seven;

import com.rabbitmq.client.Channel;
import edu.nuaa.util.RabbitMqUtils;

/**
 * @author brain
 * @version 1.0
 * @date 2023/5/8 21:15
 */
public class EmitLogTopic {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        channel.basicPublish(EXCHANGE_NAME,"quick.orange.rabbit",null,"被队列Q1 Q2接收到".getBytes("utf-8"));
        channel.basicPublish(EXCHANGE_NAME,"quick.orange.fox",null,"被队列Q1 接收到".getBytes("utf-8"));
        channel.basicPublish(EXCHANGE_NAME,"lazy.brown.fox",null,"被队列Q2 接收到".getBytes("utf-8"));
    }
}
