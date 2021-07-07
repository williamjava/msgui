package com.opensource.msgui.manager.pay;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages = {"com.opensource.msgui"})
@DubboComponentScan({"com.opensource.msgui.manager.pay.api.alipay.impl", "com.opensource.msgui.manager.pay.api.wechat.impl"})
@ImportResource(locations = {"classpath:config/DubboConfig.xml"})
public class ManagePayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagePayApplication.class, args);
    }
}