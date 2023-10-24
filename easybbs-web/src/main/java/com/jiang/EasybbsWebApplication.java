package com.jiang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.jiang"})
@MapperScan(basePackages = {"com.jiang.mapper"})
@EnableTransactionManagement
public class EasybbsWebApplication {
    public static void main(String[] args){
        SpringApplication.run(EasybbsWebApplication.class,args);
    }
}
