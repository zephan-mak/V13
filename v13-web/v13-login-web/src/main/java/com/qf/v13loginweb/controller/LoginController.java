package com.qf.v13loginweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.v13.api.IUserService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.common.utils.TokenUtils;
import com.qf.v13.entity.TUser;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @author maizifeng
 * @Date 2019/6/27
 */
@Controller
@RequestMapping("user")
public class LoginController {

    public static final String COOKIENAME="user-token";
    @Reference
    private IUserService userService;

    @RequestMapping("login")
    public String login(HttpServletRequest request){
        String referer = request.getHeader("referer");
        if(referer!=null){
            userService.keepReferer(referer);
            return "login";
        }
        return "login";
    }

    @RequestMapping("loginIn")
    public String loginIn(TUser user, HttpServletResponse response){
        ResultBean resultBean = userService.login(user);
        String referer = userService.getReferer();
        if("200".equals(resultBean.getStatusCode())){
            Cookie cookie=new Cookie(COOKIENAME, resultBean.getData().toString());
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setDomain("qf.com");
            response.addCookie(cookie);
            if(referer.contains("register")||referer.contains("login")){
                return "redirect:http://www.qf.com:9091/index/home";
            }else {
                return "redirect:"+referer;
            }
        }
        return "redirect:login";
    }

    @RequestMapping("checkLogin")
    @ResponseBody
    @CrossOrigin(origins = "*",allowCredentials = "true")
    public ResultBean checkLogin(@CookieValue(name = COOKIENAME,required = false) String uuid){
        if(uuid!=null){
            ResultBean resultBean = userService.checkLogin(uuid);

            TUser user= (TUser) resultBean.getData();
            return new ResultBean("200", user);

        }
        return new ResultBean("404", null);
    }

//    @RequestMapping("checkLoginJsonP")
//    @ResponseBody
//    public String checkLoginJsonP(@CookieValue(name = COOKIENAME,required = false) String uuid, String callBack) throws JsonProcessingException {
//        ResultBean resultBean = null;
//        if(uuid!=null){
//            resultBean = userService.checkLogin(uuid);
//        }
//        ObjectMapper objectMapper=new ObjectMapper();
//        String json = objectMapper.writeValueAsString(resultBean);
//        return callBack+"("+json+")";
//    }
//    @RequestMapping("checkLogin2")
//    @ResponseBody
//    public ResultBean checkLogin2(HttpServletRequest request){
//        Cookie[] cookies = request.getCookies();
//        if(cookies != null){
//            for (Cookie cookie : cookies) {
//                if(COOKIENAME.equals(cookie.getName())){
//                    cookie.setDomain("qf.com");
//                    String uuid=cookie.getValue();
//                    if(uuid!=null){
//                        ResultBean resultBean = userService.checkLogin(uuid);
//                        return resultBean;
//                    }
//                }
//            }
//        }
//        return new ResultBean("404", null);
//    }

    @RequestMapping("loginOut")
    @ResponseBody
    @CrossOrigin(origins = "*",allowCredentials = "true")
    public ResultBean loginOut(@CookieValue(name =COOKIENAME,required = false) String uuid, HttpServletResponse response){
        if(uuid!=null){
//            ResultBean resultBean = userService.loginOut(uuid);
            Cookie cookie=new Cookie(COOKIENAME, uuid);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            cookie.setDomain("qf.com");
            response.addCookie(cookie);
            return new ResultBean("200", "注销成功");
        }
        return new ResultBean("404", null);
    }

    @RequestMapping("getReferer")
    @ResponseBody
    public String getReferer(HttpServletRequest request){
        String referer = request.getHeader("referer");
        return referer;
    }
}
