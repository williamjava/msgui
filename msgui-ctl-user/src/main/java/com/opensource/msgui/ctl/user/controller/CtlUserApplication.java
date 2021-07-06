package com.opensource.msgui.ctl.user.controller;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableFeignClients(basePackages = {"com.opensource.msgui"})
@SpringBootApplication(scanBasePackages = {"com.opensource.msgui"})
@EnableDubbo
@ImportResource(locations = {"classpath:config/*.xml"})
public class CtlUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(CtlUserApplication.class,args);
    }

    @Bean
    public Redisson redisson() {
        //单机模式
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379").setPassword("123456").setDatabase(0);
        return (Redisson) Redisson.create(config);
    }
}