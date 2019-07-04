package com.qf.v13itemweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.IProductService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TProduct;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author maizifeng
 * @Date 2019/6/18
 */
@Controller
@RequestMapping("item")
public class ItemController {

    @Autowired
    private Configuration configuration;

    @Reference
    private IProductService productService;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @RequestMapping("createHtml/{id}")
    @ResponseBody
    public ResultBean createHtml(@PathVariable Long id){

        return CreateHtmlById(id);
    }

//    @RequestMapping("parem")
//    @ResponseBody
//    public ResultBean parem(String username,String password){
//        System.out.println(username+"---"+password);
//        return new ResultBean("200","ok！");
//    }



    private ResultBean CreateHtmlById(@PathVariable Long id) {
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

    @RequestMapping("batchCreateHtml")
    @ResponseBody
    public ResultBean batchCreateHtml(@RequestParam List<Long> idList) throws ExecutionException, InterruptedException {
        List<Future<Long>> results=new ArrayList<>(idList.size());
        for (Long id : idList) {
            results.add(threadPoolExecutor.submit(new CreateHtmlTask(id, configuration, productService)));
        }
        List<Long>errors=new ArrayList<>();
        for (Future<Long> result : results) {
            Long id=result.get();
            if(id!=0){
                errors.add(id);
            }
        }
        if(errors.size()>0){
            return new ResultBean("404","生成静态页失败！");
        }
        return new ResultBean("200","生成静态页成功！");
    }
}

class CreateHtmlTask implements Callable<Long>{

    private Long id;
    private Configuration configuration;
    private IProductService productService;

    public CreateHtmlTask(Long id, Configuration configuration, IProductService productService){
        this.id=id;
        this.configuration=configuration;
        this.productService=productService;
    }

    @Override
    public Long call() throws Exception {
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
            return id;
        } catch (TemplateException e) {
            e.printStackTrace();
            return id;
        }
        return 0L;
    }
}


