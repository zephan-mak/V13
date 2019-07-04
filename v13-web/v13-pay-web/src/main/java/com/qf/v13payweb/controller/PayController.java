package com.qf.v13payweb.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.v13payweb.pojo.PayConnect;
import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author maizifeng
 * @Date 2019/7/1
 */
@Controller
@RequestMapping("aliPay")
public class PayController {

    @Autowired
    private AlipayClient alipayClient;

    @Value("${AliPay.aliPayPublicKey}")
    private String aliPayPublicKey;

    @RequestMapping("pay")
    public void pay( HttpServletResponse response,String orderId) throws IOException {
        AlipayTradePagePayRequest aliPayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        aliPayRequest.setReturnUrl("http://domain.com/CallBack/return_url.jsp");
        aliPayRequest.setNotifyUrl("http://zephan.zicp.vip/aliPay/notify");//在公共参数中设置回跳和通知地址
        PayConnect payConnect=new PayConnect(orderId, "FAST_INSTANT_TRADE_PAY", "88.88", "Iphone6 16G","Iphone6 16G" );
        ObjectMapper objectMapper=new ObjectMapper();
        String json = objectMapper.writeValueAsString(payConnect);
        aliPayRequest.setBizContent(json);//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(aliPayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8" );
        response.getWriter().write(form);//直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();
    }

    @RequestMapping("notify")
    public void notifyAliPay(HttpServletRequest request,HttpServletResponse response) throws AlipayApiException, IOException {
        Map<String, String[]> resultMap = request.getParameterMap();
        Map<String, String> paramsMap =new HashMap<>(); //将异步通知中收到的所有参数都存放到map中
        Set<Map.Entry<String, String[]>> entrySet = resultMap.entrySet();
        for (Map.Entry<String, String[]> entry : entrySet) {
            StringBuilder value=new StringBuilder();
            String[] valueArray = entry.getValue();
            for (int i = 0; i < valueArray.length-1; i++) {
                value.append(valueArray[i]).append(",");
            }
            value.append(valueArray[valueArray.length-1]);
            paramsMap.put(entry.getKey(), value.toString());
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, aliPayPublicKey, "utf-8", "RSA2"); //调用SDK验证签名
        if(signVerified){
            System.out.println("是支付宝发过来的信息");
// TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            String orderId=request.getParameter("out_trade_no");
            String money=request.getParameter("total_amount");
            String trade_no=request.getParameter("trade_no");

            PrintWriter writer = response.getWriter();
            writer.write("success");
            writer.flush();
            writer.close();
        }else{
// TODO 验签失败则记录异常日志，并在response中返回failure.
            System.out.println("不是支付宝发过来的");
            PrintWriter writer = response.getWriter();
            writer.write("fail");
            writer.flush();
            writer.close();
        }
    }

}
