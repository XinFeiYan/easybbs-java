package com.jiang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//添加包扫描
@SpringBootApplication(scanBasePackages = {"com.jiang"})
@MapperScan(basePackages = {"com.jiang.mapper"})
//支持注解事务
@EnableTransactionManagement
public class EasybbsAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasybbsAdminApplication.class,args);
    }
}
