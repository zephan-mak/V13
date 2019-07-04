package com.qf.v13indexweb;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.IProductTypeService;
import com.qf.v13.entity.TProductType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author maizifeng
 * @Date 2019/6/14
 */
@Controller
@RequestMapping("index")
public class HomeController {
    @Reference
    private IProductTypeService productTypeService;

    @RequestMapping("home")
    public String showHome(Model model, HttpServletRequest request){
        List<TProductType> productTypeList = productTypeService.list();
        model.addAttribute("list", productTypeList);
        return "home";
    }
}
