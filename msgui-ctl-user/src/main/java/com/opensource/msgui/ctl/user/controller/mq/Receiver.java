package com.opensource.msgui.ctl.user.controller.mq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author whj
 *
 * 消息消费者
 *
 * RabbitListener对队列hello进行监听，RabbitHandler指定对消息的处理方法；
 */
@Component
@RabbitListener(queues = "hello")
public class Receiver {
    @RabbitHandler
    public void process(String msg) {
        System.out.println("消费者开始消费消息了......");
        System.out.println("消费者接收到的消息为：" + msg);
    }
}