package com.qf.v13loginweb;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class V13LoginWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(V13LoginWebApplication.class, args);
	}

}
