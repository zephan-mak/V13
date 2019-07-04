package com.qf.temptimer.jdk;

import java.util.Date;
import java.util.Timer;

/**
 * @author maizifeng
 * @Date 2019/6/26
 */
public class main {
    public static void main(String[] args){
        Timer timer=new Timer();
        MyTimerTask task=new MyTimerTask();
        System.out.println(new Date());
        timer.schedule(task, 1000,1000) ;
    };
}
