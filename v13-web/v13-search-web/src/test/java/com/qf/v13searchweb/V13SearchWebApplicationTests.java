package com.qf.v13searchweb;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.api.ISearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13SearchWebApplicationTests {

    @Reference
    private ISearchService searchService;
    @Test
    public void contextLoads() {
        ResultBean resultBean = searchService.updateById(35L);
        System.out.println(resultBean.getData());
    }

}
