package com.bitcom.api.controller;

import com.bitcom.api.service.IRefundService;
import com.bitcom.common.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RefundController {
    @Autowired
    @Qualifier("commonRefundServiceImpl")
    private IRefundService refundService;

    @RequestMapping({"/payService/refund"})
    public ReturnObject refund(String outTradeNo, String outRefundNo, String refundFee) {
        try {
            return this.refundService.refund(outTradeNo, outRefundNo, refundFee);
        } catch (Exception e) {
            return ReturnObject.error(e.getMessage());
        }
    }
}



