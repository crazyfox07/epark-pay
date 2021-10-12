package com.bitcom.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.api.service.ApiGatewayService;
import com.bitcom.common.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ApiPaymentController {
    @Autowired
    @Qualifier("commonPayService")
    private ApiGatewayService commonPayService;

    @RequestMapping({"/payService/payment"})
    public ReturnObject payment(@RequestBody JSONObject json) {
        try {
            return this.commonPayService.pay(json);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnObject.error(e.getMessage());
        }
    }
}



