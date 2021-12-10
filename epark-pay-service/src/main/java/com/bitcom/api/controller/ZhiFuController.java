package com.bitcom.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.common.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@RestController
public class ZhiFuController {
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping({"/epark/payment"})
    public String payment(String position, String parkCode, String devCode) {
        try {
            JSONObject attach = new JSONObject();
            attach.put("payTypeDerate", 2.0);
            attach.put("orderType", "8202");
            attach.put("orderNo","202110271604114761");
            attach.put("payType", "0817");
            attach.put("needPay", 2.0);
            attach.put("payScheme", "0611");
            attach.put("parkCode", "371104");
            attach.put("couponId", "");
            attach.put("couponDerate", 0.0);
            attach.put("payScene", "1305");

            JSONObject params = new JSONObject();
            params.put("totalAmount", "0.01");
            params.put("authCode", "");
            params.put("subject", "停车费");
            params.put("payIp", "");
            params.put("mid", "");
            params.put("body", "停车费2.0元");
            params.put("buyerId", "");
            params.put("tid", "");

            JSONObject bizProtocol = new JSONObject();
            bizProtocol.put("bizName", "parkFee");
            bizProtocol.put("attach", attach);
            bizProtocol.put("params", params);

            JSONObject body = new JSONObject();
            body.put("payScheme", "0611");
            body.put("payApiChannel", "1002");
            body.put("ver", "3.0");
            body.put("bizProtocol", bizProtocol);

            ReturnObject response = restTemplate.postForObject("http://127.0.0.1:3605/payService/payment", body, ReturnObject.class);
            return (String) response.get("data");
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @RequestMapping({"/hello"})
    public String hello(HttpServletRequest request) {
        try {
            System.out.println("hello world");
            System.out.println(request.getHeaderNames());
            System.out.println(request.getQueryString());
            return "hello world";
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @RequestMapping({"/home_page"})
    public String homePage(HttpServletRequest request) {
        try {
            System.out.println("home page");
            System.out.println(request.getHeaderNames());
            System.out.println(request.getQueryString());
            return "home page";
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }


    @RequestMapping({"/thirdPark/thirdParkQRScan.do"})
    public String saomazifu(HttpServletRequest request){
        System.out.println(request.getQueryString());
        Enumeration<String> e = request.getHeaderNames();
        while(e.hasMoreElements()){
            String headerName = e.nextElement();//透明称
            Enumeration<String> headerValues = request.getHeaders(headerName);
            while(headerValues.hasMoreElements()){
                System.out.println(headerName+":"+headerValues.nextElement());
            }
        }
        System.out.println(request.getHeader("cookie"));
        System.out.println(request.getHeader("ccbwebview-user-agent"));
        System.out.println(request.getHeader("ccbwebview-user-agent") != null);
        System.out.println(request.getHeader("ccbwebview-user-agent1") != null);
        String position = request.getParameter("exitPark");
        String parkCode = request.getParameter("parkCode");
        String devCode = request.getParameter("devCode");
        // todo 待将！=改成==
        if(request.getHeader("ccbwebview-user-agent") != null) {  // 建行支付

             String resStr = this.payment(position, parkCode, devCode);
             return resStr;
        } else {
            return "请使用其它支付";
        }

    }
}
