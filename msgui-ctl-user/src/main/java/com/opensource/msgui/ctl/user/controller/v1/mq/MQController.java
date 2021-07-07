package com.opensource.msgui.ctl.user.controller.v1.mq;

import com.opensource.msgui.commons.response.ResponseResult;
import com.opensource.msgui.ctl.user.controller.mq.Sender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author whj
 *
 * MQ实践控制器
 */
@RestController
@RequestMapping("/api/mq/v1")
public class MQController {
    @Resource
    private Sender sender;

    @PostMapping("send")
    public ResponseResult send() {
        sender.send();
        return ResponseResult.success("异步消息发送成功");
    }
}

    
