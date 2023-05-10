package edu.nuaa.four;

import com.rabbitmq.client.Channel;
import edu.nuaa.util.RabbitMqUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author brain
 * @version 1.0
 * @date 2023/5/7 10:21
 * 可靠传输：
 * 1.队列持久化
 * 2.消息持久化
 * 3.发布确认（未保存到磁盘 但宕机）
 * 发布确认：
 * 1.单个确认
 * 2.批量确认
 * 3.异步批量确认
 */
public class PublishConfirm {
    //批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        publishMessageIndividually();//发布1000个单独确认消息，耗时:856ms
        publishMessageBatch();//发布1000个批量确认消息，耗时:205ms
        publishMessageAsync();//发布1000个异步确认消息，耗时:116ms
    }

    //单个确认
    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启信道的发布确认
        channel.confirmSelect();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            //单个消息进行发布确认
            boolean flag = channel.waitForConfirms();
            if (!flag){
                System.out.println(message + "发送失败");
                break;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时:" +  (end - begin) + "ms");
    }

    //批量确认
    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启信道的发布确认
        channel.confirmSelect();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            if (((i + 1 ) % 100) == 0)channel.waitForConfirms();
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时:" +  (end - begin) + "ms");
    }

    //异步确认
    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启信道的发布确认
        channel.confirmSelect();
        /**
         * 异步回调为了确保能够重新发送未确认成功的消息，需要手动保存
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirm = new ConcurrentSkipListMap<>();
        //消息监听器
        channel.addConfirmListener((long deliveryTag, boolean multiple)->{
            //成功
//            System.out.println("确认成功消息:" + deliveryTag);
            if (multiple){
                ConcurrentNavigableMap<Long, String> confirm = outstandingConfirm.headMap(deliveryTag);
                confirm.clear();
            }else {
                outstandingConfirm.remove(deliveryTag);
            }
        },(long deliveryTag, boolean multiple)->{
            //失败
            String message = outstandingConfirm.get(deliveryTag);
            System.out.println("未确认成功消息Tag:" + deliveryTag + "内容:" + message);

        });
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            outstandingConfirm.put(channel.getNextPublishSeqNo(),message);
            channel.basicPublish("",queueName,null,message.getBytes());
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息，耗时:" +  (end - begin) + "ms");
    }

}
