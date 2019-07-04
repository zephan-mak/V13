package com.qf.v13registerweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.IUserService;
import com.qf.v13.common.constant.RabbitMQConstant;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.common.utils.CodeUtils;
import com.qf.v13.entity.TUser;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author maizifeng
 * @Date 2019/6/25
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Reference
    private IUserService userService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoderl;

    @RequestMapping("register")
    public String register1(HttpServletRequest request, HttpServletResponse response) {
//        getCode(request, response);
        return "register";
    }

//    private void getCode(HttpServletRequest request, HttpServletResponse response) {
//        response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
//        response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
//        response.setHeader("Cache-Control", "no-cache");
//        response.setDateHeader("Expire", 0);
//        CodeUtils randomValidateCode = new CodeUtils();
//        randomValidateCode.getCode(request, response);//输出验证码图片
//        //将生成的随机验证码存放到redis中
//        redisTemplate.opsForValue().set("randomCode", (String) request.getSession().getAttribute("RANDOMCODE"), Long.parseLong("60000"));
//    }

    @RequestMapping("register2")
    public String register2() {

        return "register2";
    }

    @PostMapping("add")
    public String add(TUser user) {
        user.setPassword(passwordEncoderl.encode(user.getPassword()));
        int id = userService.insertSelective(user);
        if (id > 0) {
            Context context = new Context();
            context.setVariable("username", user.getUsername());
            String uuid = UUID.randomUUID().toString();
            String href = new StringBuilder("http://localhost:9094/user/action/").append(uuid).toString();
            context.setVariable("href", href);
            redisTemplate.opsForValue().set(uuid, id);
            String text = templateEngine.process("test.html", context);
            Map<String, String> map = new HashMap<>();
            map.put("to", user.getEmail());
            map.put("subject", "激活邮件");
            map.put("text", text);
            rabbitTemplate.convertAndSend(RabbitMQConstant.REGISTER_EXCHANGE, "user.add", map);

            return "success";
        }
        return "register2";
    }

    @RequestMapping("action/{uuid}")
    @ResponseBody
    public String activation(@PathVariable("uuid") String uuid) {
        ResultBean resultBean = userService.activation(uuid);
        if (resultBean.getStatusCode().equals("200")) {
            return "激活成功";
        } else {
            return "激活失败";
        }

    }

    @PostMapping("getCode/{phone}")
    @ResponseBody
    public ResultBean getCode(@PathVariable("phone") String phone) {
        rabbitTemplate.convertAndSend(RabbitMQConstant.REGISTER_EXCHANGE,"phoneCode.get",phone);
        return new ResultBean("200", "发送成功");
    }

    @PostMapping("addByPhone")
    public String add2(TUser user, @RequestParam("phoneCode") String phoneCode) {
        user.setPassword(passwordEncoderl.encode(user.getPassword()));
        ResultBean resultBean = userService.eqCode(user, phoneCode);
        if (resultBean.getStatusCode().equals("200")){
            return "redirect:http://sso.qf.com:9095/user/login";
        }
        return "register";
    }

    @PostMapping("addUser")
    public String addUser(TUser user, @RequestParam("phoneCode") String phoneCode){
        user.setPassword(passwordEncoderl.encode(user.getPassword()));
        ResultBean resultBean = userService.eqCode(user, phoneCode);
        int id=(int)resultBean.getData();
        if (id > 0) {
            Context context = new Context();
            context.setVariable("username", user.getUsername());
            String uuid = UUID.randomUUID().toString();
            String href = new StringBuilder("http://localhost:9094/user/action/").append(uuid).toString();
            context.setVariable("href", href);
            redisTemplate.opsForValue().set(uuid, id);
            String text = templateEngine.process("test.html", context);
            Map<String, String> map = new HashMap<>();
            map.put("to", user.getEmail());
            map.put("subject", "激活邮件");
            map.put("text", text);
            rabbitTemplate.convertAndSend(RabbitMQConstant.REGISTER_EXCHANGE, "user.add", map);


            if (resultBean.getStatusCode().equals("200")){
                return "success";
            }
        }

        return "register";
    }

//    @RequestMapping("/createImg")
//    public void createImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        getCode(request, response);
//    }

}
