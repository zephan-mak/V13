package com.qf.v13centerweb.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qf.v13.api.IProductService;
import com.qf.v13.common.constant.RabbitMQConstant;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.common.utils.HttpClientUtils;
import com.qf.v13.entity.TProduct;
import com.qf.v13.entity.TProductType;
import com.qf.v13.pojo.TProductVO;
import com.qf.v13.api.ISearchService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author maizifeng
 * @Date 2019/6/12
 */
@Controller
@RequestMapping("product")
public class ProductController {


    @Reference
    private IProductService productService;
    @Reference
    private ISearchService searchService;
    @Autowired
    private RabbitTemplate template;

    @RequestMapping("page/{indexPage}/{pageSize}")
    public String page(@PathVariable("indexPage")int indexPage,
                       @PathVariable("pageSize")int pageSize, Model model){
        PageInfo<TProduct> page = productService.page(indexPage, pageSize);
        model.addAttribute("page",page);
        return "product/list";
    }

    @PostMapping("add")
    public String  add(TProductVO productVO){
        System.out.println(productVO);
        Long id = productService.save(productVO);
//        searchService.updateById(id);
//        HttpClientUtils.doGet("http://localhost:9093/item/createHtml/"+id);
        template.convertAndSend(RabbitMQConstant.CENTER_PRODUCT_EXCHANGE, "search.add", id);
        template.convertAndSend(RabbitMQConstant.CENTER_PRODUCT_EXCHANGE , "createHtml.byId",id);

        return "redirect:/product/page/1/7";
    }

    @PostMapping("delProduct/{id}")
    @ResponseBody
    public ResultBean del(@PathVariable("id")Long id){
        int count = productService.deleteByPrimaryKey(id);
        if (count > 0) {
            return  new ResultBean("200", "删除成功");
        } else {
            return  new ResultBean("404", "删除失败");
        }
    }

    @PostMapping("batchDel")
    @ResponseBody
    public ResultBean  batchDel(@RequestParam ("list") List<Long> list){
        long count = productService.batchDel(list);
         if (count > 0) {
             return  new ResultBean("200", "删除成功");
         } else {
             return  new ResultBean("404", "删除失败");
        }
    }
//    @PostMapping("type")
//    @ResponseBody
//    public List<TProductType> Type(Model model){
//        List<TProductType> productTypeList= productService.getType();
//        return productTypeList;
//    }
    @PostMapping("type")
    @ResponseBody
    public String Type(Model model){
        List<TProductType> productTypeList= productService.getType();

        JSONArray array=new JSONArray();
        for (TProductType productType : productTypeList) {
            JSONObject json=new JSONObject();
            json.put("id", productType.getId());
            json.put("name", productType.getName());
            array.add(json);
        }

            return array.toString();


    }
    @PostMapping("modify/{id}")
    @ResponseBody
    public TProductVO toUpdate(@PathVariable Long id, Model model){
        TProductVO productVO = productService.selectAllById(id);
        model.addAttribute("productVO", productVO);
        return productVO;
    }

    @PostMapping("update/{id}")
    public String update(@PathVariable Long id ,TProductVO productVO){
        productVO.getProduct().setId(id);
        long updateId = productService.updateById(id, productVO);
        template.convertAndSend(RabbitMQConstant.CENTER_PRODUCT_EXCHANGE, "search.add", updateId);
        return "redirect:/product/page/1/7";
    }



}
