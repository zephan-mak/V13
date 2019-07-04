package com.qf.v13registerweb;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableDubbo
@EnableWebSecurity
public class V13RegisterWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(V13RegisterWebApplication.class, args);
    }

}
