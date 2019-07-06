package com.qf.v13loginweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.IUserService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
            String uuid=resultBean.getData().toString();
            RefreshCookie(response, uuid);
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
    public ResultBean checkLogin(@CookieValue(name = COOKIENAME,required = false) String uuid, HttpServletResponse response){
        if(uuid!=null){
            ResultBean resultBean = userService.checkLogin(uuid);
            if("200".equals(resultBean.getStatusCode())){
                HashMap<String,Object> data = (HashMap<String, Object>) resultBean.getData();
                TUser user = (TUser) data.get("user");
                uuid = (String) data.get("uuid");
                RefreshCookie(response, uuid);
                return new ResultBean("200", user);
            }else{
                delCookie(uuid, response);
                return new ResultBean("404", resultBean.getData());
            }

        }
        return new ResultBean("500", null);
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
            delCookie(uuid, response);
            return new ResultBean("200", "注销成功");
        }
        return new ResultBean("404", null);
    }
    private void RefreshCookie(HttpServletResponse response, String uuid) {
        Cookie cookie = new Cookie(COOKIENAME, uuid);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setDomain("qf.com");
        response.addCookie(cookie);
    }
    private void delCookie(@CookieValue(name = COOKIENAME, required = false) String uuid, HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIENAME, uuid);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setDomain("qf.com");
        response.addCookie(cookie);
    }

    @RequestMapping("getReferer")
    @ResponseBody
    public String getReferer(HttpServletRequest request){
        String referer = request.getHeader("referer");
        return referer;
    }
}
