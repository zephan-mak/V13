package com.qf.v13cartweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.ICartService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author maizifeng
 * @Date 2019/6/30
 */
@Controller
@RequestMapping("cart")
public class CartController {

    @Reference
    private ICartService cartService;

    @RequestMapping("add/{productId}/{count}")
    @ResponseBody
    public ResultBean add(@PathVariable long productId, @PathVariable int count,
                          @CookieValue(name = "user_cart",required = false)String uuid, HttpServletResponse response,
                          HttpServletRequest request){
        TUser user = (TUser) request.getAttribute("user");
        /**
         * 已登录的购物车
         */
        if(user!=null){
            String touristUuid=uuid;
            uuid=user.getId().toString();
//            ResultBean resultBean = cartService.add(uuid, productId, count);
            ResultBean resultBean = cartService.add(uuid, productId, count,touristUuid);
            return resultBean;
        }
        /**
         * 判断是否有购物车
         */
        if(uuid==null||"".equals(uuid)){
            uuid= UUID.randomUUID().toString();
        }
        ResultBean resultBean = cartService.add(uuid, productId, count);
        /**
         * 刷新Cookie
         */
        RefreshCookie(uuid, response, resultBean);
        return resultBean;
    }


    @RequestMapping("del/{productId}")
    @ResponseBody
    public ResultBean del(@PathVariable long productId,
                          @CookieValue(name = "user_cart",required = false)String uuid,HttpServletResponse response,
                          HttpServletRequest request){
        TUser user = (TUser) request.getAttribute("user");
        /**
         * 已登录的购物车
         */
        if(user!=null){
            uuid=user.getId().toString();
            ResultBean resultBean = cartService.del(uuid, productId);
            return resultBean;
        }
        /**
         * 判断是否有购物车
         */
        if (CheckHasCart(uuid)) return new ResultBean("404", "购物车为空");
        ResultBean resultBean = cartService.del(uuid, productId);
        /**
         * 刷新Cookie
         */
        RefreshCookie(uuid, response, resultBean);
        return resultBean;
    }

    @RequestMapping("select")
    @ResponseBody
    public ResultBean select( @CookieValue(name = "user_cart",required = false)String uuid,HttpServletResponse response,
                              HttpServletRequest request){
        TUser user = (TUser) request.getAttribute("user");
        /**
         * 已登录的购物车
         */
        if(user!=null){
            uuid=user.getId().toString();
            ResultBean resultBean = cartService.select(uuid);
            return resultBean;
        }
        /**
         * 判断是否有购物车
         */
        if (CheckHasCart(uuid)) return new ResultBean("404", "购物车为空");
        ResultBean resultBean = cartService.select(uuid);
        /**
         * 刷新Cookie
         */
        RefreshCookie(uuid, response, resultBean);
        return resultBean;
    }


    @RequestMapping("update/{productId}/{count}")
    @ResponseBody
    public ResultBean update(@PathVariable long productId, @PathVariable int count,
                             @CookieValue(name = "user_cart",required = false)String uuid, HttpServletResponse response,
                             HttpServletRequest request){
        TUser user = (TUser) request.getAttribute("user");
        /**
         * 已登录的购物车
         */
        if(user!=null){
            uuid=user.getId().toString();
            ResultBean resultBean = cartService.update(uuid, productId, count);
            return resultBean;
        }
        /**
         * 判断是否有购物车
         */
        if (CheckHasCart(uuid)) return new ResultBean("404", "购物车为空");
        ResultBean resultBean = cartService.update(uuid, productId, count);
        /**
         * 刷新Cookie
         */
        RefreshCookie(uuid, response, resultBean);
        return resultBean;
    }

    private boolean CheckHasCart(@CookieValue(name = "user_cart", required = false) String uuid) {
        if (uuid == null || "".equals(uuid)) {
            return true;
        }
        return false;
    }

    private void RefreshCookie(@CookieValue(name = "user_cart", required = false) String uuid, HttpServletResponse response, ResultBean resultBean) {
        if ("200".equals(resultBean.getStatusCode())) {
            Cookie cookie = new Cookie("user_cart", uuid);
            cookie.setPath("/");
            cookie.setDomain("qf.com");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(cookie);
        }
    }
}
