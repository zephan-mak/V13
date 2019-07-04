package com.qf.temptimer.springboot;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author maizifeng
 * @Date 2019/6/26
 */
@Component
public class MyTimerTask {

    @Scheduled(cron = "0,3,4 * * * * ? ")
    public void Test1(){
        System.out.println(Thread.currentThread().getName()+"----"+new Date());
    }
}
