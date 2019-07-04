package com.qf.v13centerweb;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13CenterWebApplicationTests {

	@Autowired
	private FastFileStorageClient client;

	@Test
	public void contextLoads() throws FileNotFoundException {

        File file=new File("F:\\Java培训\\workspace4\\v13\\v13-web\\v13-center-web\\hello.html");
        StorePath storePath = client.uploadFile(new FileInputStream(file), file.length(), "html", null);
        System.out.println(storePath.getFullPath());

    }

    @Test
    public void del() throws FileNotFoundException {

        client.deleteFile("group1/M00/00/00/wKjYgV0CH5SAIKc1AAAAj1Ik7Rg67.html");
	}
}
