package com.qf.tempbcrypt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TempBcryptApplicationTests {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void contextLoads() {
        String asd = passwordEncoder.encode("admin");
        System.out.println(asd);
        System.out.println(passwordEncoder.matches("admin", asd));

    }

}
