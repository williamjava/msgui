package com.opensource.msgui.auth.server;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication(scanBasePackages = {"com.opensource.msgui"})
@EnableDubbo
@ImportResource(locations = {"classpath:config/*.xml"})
@EnableFeignClients(basePackages = {"com.opensource.msgui"})
public class AuthServerApplication {
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    public static void main(String[] args) {
        try{
            SpringApplication.run(AuthServerApplication.class,args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}