package com.qf.v13cartweb.Interceptor;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.ICartService;
import com.qf.v13.api.IUserService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @author maizifeng
 * @Date 2019/7/1
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Reference
    private IUserService userService;

    @Reference
    private ICartService cartService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length>0){
            String touristUuid=null;
            for (Cookie cookie : cookies) {
                if("user_cart".equals(cookie.getName())){
                    touristUuid=cookie.getValue();
                }
                if("user-token".equals(cookie.getName())){
                    String uuid=cookie.getValue();
                    ResultBean resultBean = userService.checkLogin(uuid);
                    TUser user = (TUser) resultBean.getData();
                    if("200".equals(resultBean.getStatusCode())){
                        request.setAttribute("user", user);
                    }
                        cartService.merge(touristUuid,user.getId());
                }

            }
        }
        return true;
    }
}
