package com.qf.temptimer.jdk;

import java.util.Date;
import java.util.TimerTask;

/**
 * @author maizifeng
 * @Date 2019/6/26
 */
public class MyTimerTask extends TimerTask {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"----"+new Date());
    }
}
