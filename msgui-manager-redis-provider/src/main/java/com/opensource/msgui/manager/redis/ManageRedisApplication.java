package com.opensource.msgui.manager.redis;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages = {"com.opensource.msgui"})
@DubboComponentScan("com.opensource.msgui.manager.redis.api.impl")
@ImportResource(locations = {"classpath:config/DubboConfig.xml"})
public class ManageRedisApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageRedisApplication.class, args);
    }
}