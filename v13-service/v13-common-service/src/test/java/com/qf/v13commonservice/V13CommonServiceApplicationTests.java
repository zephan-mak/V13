package com.qf.v13commonservice;

import com.qf.v13.api.IEmailService;
import com.qf.v13.common.pojo.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13CommonServiceApplicationTests {

    @Autowired
    private IEmailService emailService;

    @Test
    public void contextLoads() {
        ResultBean resultBean = emailService.send("a358866032@gmail.com", "服务发布测试", "服务发布成功");
        System.out.println(resultBean);
    }

}
