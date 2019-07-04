package com.qf.v13userservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@MapperScan("com.qf.v13.mapper")
@EnableDubbo
@EnableWebSecurity
public class V13UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(V13UserServiceApplication.class, args);
    }

}
