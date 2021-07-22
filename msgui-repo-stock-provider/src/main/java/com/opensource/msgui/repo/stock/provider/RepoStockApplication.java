package com.opensource.msgui.repo.stock.provider;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import io.seata.rm.datasource.DataSourceProxy;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@SpringBootApplication(scanBasePackages = {"com.opensource.msgui"}, exclude = DataSourceAutoConfiguration.class)
@MapperScan(basePackages ={ "com.opensource.msgui.repo.stock.provider.mapper"})
@DubboComponentScan("com.opensource.msgui.repo.stock.provider.impl")
@ImportResource(locations = {"classpath:config/DubboConfig.xml"})
public class RepoStockApplication {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource(){
        return new DruidDataSource();
    }

    @Primary
    @Bean("dataSource")
    public DataSourceProxy dataSource(DataSource druidDataSource){
        return new DataSourceProxy(druidDataSource);
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    public static void main(String[] args) {
        SpringApplication.run(RepoStockApplication.class, args);
    }
}
