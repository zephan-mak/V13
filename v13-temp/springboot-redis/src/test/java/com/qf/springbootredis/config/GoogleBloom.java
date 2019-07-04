package com.qf.springbootredis.config;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author maizifeng
 * @Date 2019/7/4
 */
@Component
public class GoogleBloom {


    private BloomFilter bloomFilter;

    @PostConstruct
    public void init(){
        List<Long> list=new ArrayList<>();
        for (long i = 0; i < 100; i++) {
            list.add(i);
        }

        bloomFilter = BloomFilter.create(Funnels.longFunnel(), list.size(), 0.01);

        for (Long a : list) {
            bloomFilter.put(a);
        }
    }

    public boolean isExists(Long id){
        return bloomFilter.mightContain(id);
    }
}
