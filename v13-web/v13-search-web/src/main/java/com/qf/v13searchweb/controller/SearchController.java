package com.qf.v13searchweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.api.ISearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author maizifeng
 * @Date 2019/6/17
 */
@Controller
@RequestMapping("search")
public class SearchController {
    @Reference
    private ISearchService searchService;
    @RequestMapping("searchKeyWord")
    public String searchKeyWord(String keyWord, Model model){
        ResultBean resultBean = searchService.searchKeyWord(keyWord);
        model.addAttribute("result", resultBean);
        return "list";
    }
    @RequestMapping("page/{indexPage}/{pageSize}/searchKeyWord")
    public String searchKeyWord(@PathVariable("indexPage")int indexPage,
                                @PathVariable("pageSize")int pageSize,String keyWord, Model model){
        ResultBean resultBean = searchService.searchKeyWord(keyWord, indexPage, pageSize);
        model.addAttribute("result", resultBean);
        model.addAttribute("keyWord", keyWord);
        return "list";
    }
}
