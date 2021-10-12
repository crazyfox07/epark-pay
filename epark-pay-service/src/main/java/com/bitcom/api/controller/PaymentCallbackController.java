package com.bitcom.api.controller;

import com.bitcom.pay.alipay.service.IAlipayCallbackService;
import com.bitcom.pay.chinaums.service.IChinaUmsPayNotifyService;
import com.bitcom.pay.swift.service.ISwiftAliPayCallbackService;
import com.bitcom.pay.swift.service.ISwiftCloudPayCallbackService;
import com.bitcom.pay.swift.service.ISwiftWechatPayCallbackService;
import com.bitcom.pay.wechat.service.IWechatCallbackService;
import com.bitcom.sdk.wechat.ResponseHandler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class PaymentCallbackController {
    @Autowired
    private IAlipayCallbackService alipayCallbackService;
    @Autowired
    private IWechatCallbackService wechatCallbackService;
    @Autowired
    private ISwiftWechatPayCallbackService swiftWechatPayCallbackService;
    @Autowired
    private ISwiftAliPayCallbackService swiftAliPayCallbackService;
    @Autowired
    private IChinaUmsPayNotifyService chinaUmsPayNotifyService;
    @Autowired
    private ISwiftCloudPayCallbackService swiftCloudPayCallbackService;

    @RequestMapping({"/alipay/payNotify"})
    @ResponseBody
    public String alipayNotify(HttpServletRequest request) {
        try {
            return this.alipayCallbackService.alipayCallbackHandler(request);
        } catch (Exception e) {
            return "failure";
        }
    }

    @RequestMapping({"/wechat/payNotify"})
    @ResponseBody
    public String wechatPayNotify(HttpServletRequest request) {
        try {
            return this.wechatCallbackService.wechatCallbackHandler(request);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHandler.fail();
        }
    }

    @RequestMapping({"/swift/wechat/payNotify"})
    @ResponseBody
    public String swiftWechatPayNotify(HttpServletRequest request) {
        try {
            return this.swiftWechatPayCallbackService.wechatCallbackHandler(request);
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping({"/swift/alipay/payNotify"})
    @ResponseBody
    public String swiftAliPayNotify(HttpServletRequest request) {
        try {
            return this.swiftAliPayCallbackService.alipayCallbackHandler(request);
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping({"/swift/cloudPay/payNotify"})
    @ResponseBody
    public String swiftCloudPayNotify(HttpServletRequest request) {
        try {
            return this.swiftCloudPayCallbackService.cloudPayCallbackHandler(request);
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping({"/chinaUms/payNotify"})
    @ResponseBody
    public String chinaUmsPayNotify(HttpServletRequest request) {
        try {
            return this.chinaUmsPayNotifyService.chinaUmsPayCallbackHandler(request);
        } catch (Exception e) {
            e.printStackTrace();
            return "FAILED";
        }
    }
}



