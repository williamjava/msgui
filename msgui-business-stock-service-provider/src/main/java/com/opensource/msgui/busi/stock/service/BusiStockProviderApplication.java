package com.opensource.msgui.busi.stock.service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = {"com.opensource.msgui"})
@EnableDubbo
@ImportResource(locations = {"classpath:config/*.xml"})
@EnableFeignClients(basePackages = {"com.opensource.msgui"})
public class BusiStockProviderApplication {
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    public static void main(String[] args) {
        try{
            SpringApplication.run(BusiStockProviderApplication.class,args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}