package com.opensource.msgui.ctl.order.controller;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ImportResource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableFeignClients(basePackages = {"com.opensource.msgui"})
@SpringBootApplication(scanBasePackages = {"com.opensource.msgui"})
@EnableDubbo
@ImportResource(locations = {"classpath:config/*.xml"})
public class CtlOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(CtlOrderApplication.class,args);
    }
}