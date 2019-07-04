package com.qf.v13itemweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author maizifeng
 * @Date 2019/6/20
 */
@Configuration
public class Config {
    @Bean
    public ThreadPoolExecutor initThreadPoolExecutor(){
        int cpus = Runtime.getRuntime().availableProcessors();

        ThreadPoolExecutor pool = new ThreadPoolExecutor(cpus, cpus*2, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100) );
        return pool;
    }
}
