package com.qf.v13itemweb;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.IProductService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TProduct;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.concurrent.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class V13ItemWebApplicationTests {

    @Autowired
    private Configuration configuration;

    @Reference
    private IProductService productService;


    @Test
    public void batchCreateHtml() throws ExecutionException, InterruptedException {
        List<Long> idList=new ArrayList<>();
        idList.add(30L);
        idList.add(31L);
        idList.add(32L);
        int cpus = Runtime.getRuntime().availableProcessors();

        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, cpus*2, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100) );

        for (Long id : idList) {
            Future<ResultBean> submit = pool.submit(new CreateHtmlTask(id, configuration, productService));
            submit.get();
            System.out.println(submit.get());
        }
    }
}

class CreateHtmlTask implements Callable<ResultBean> {

    private Long id;
    private Configuration configuration;
    private IProductService productService;

    public CreateHtmlTask(Long id, Configuration configuration, IProductService productService){
        this.id=id;
        this.configuration=configuration;
        this.productService=productService;
    }

    @Override
    public ResultBean call() throws Exception {
        TProduct product=productService.selectByPrimaryKey(id);
        try {
            Template template = configuration.getTemplate("item.ftl");
            Map<String,Object> date=new HashMap<>();
            date.put("product", product);
            String path = ResourceUtils.getURL("classpath:static").getPath();
            String filepath=new StringBuilder(path).append(File.separator).append(id).append(".html").toString();
            FileWriter writer=new FileWriter(filepath);
            template.process(date, writer);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultBean("404","读取模板失败");
        } catch (TemplateException e) {
            e.printStackTrace();
            return new ResultBean("404","生成静态页面失败");
        }
        return new ResultBean("200","生成静态页成功！");
    }

}
