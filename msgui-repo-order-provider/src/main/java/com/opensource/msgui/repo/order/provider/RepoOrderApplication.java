package com.opensource.msgui.repo.order.provider;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages = {"com.opensource.msgui"})
@MapperScan(basePackages ={ "com.opensource.msgui.repo.order.provider.mapper"})
@DubboComponentScan("com.opensource.msgui.repo.order.provider.impl")
@ImportResource(locations = {"classpath:config/DubboConfig.xml"})
public class RepoOrderApplication {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    public static void main(String[] args) {
        SpringApplication.run(RepoOrderApplication.class, args);
    }
}
