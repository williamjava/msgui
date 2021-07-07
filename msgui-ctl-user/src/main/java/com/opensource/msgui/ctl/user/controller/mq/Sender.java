package com.opensource.msgui.ctl.user.controller.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author whj
 *
 * 消息生产者
 */
@Component
public class Sender {
 
    @Autowired
    private AmqpTemplate rabbitmqTemplate;
 
    public void send(){
        String content = "hello，my name is william. Current Date is: " + new Date();
        System.out.println("生产者发布一条消息：" +content);
        this.rabbitmqTemplate.convertAndSend("hello", content);
    }
}