package com.bitcom.api.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.api.service.ApiGatewayService;
import com.bitcom.common.ReturnObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("chinaUmsCommonPayServiceImpl")
public class ChinaUmsCommonPayServiceImpl
        implements ApiGatewayService {
    @Autowired
    @Qualifier("chinaUmsAliPayServiceImpl")
    private ApiGatewayService aliPayServiceImpl;
    @Autowired
    @Qualifier("chinaUmsWechatPayServiceImpl")
    private ApiGatewayService wechatServiceImpl;

    public ReturnObject pay(JSONObject protocol) throws Exception {
        String payType = protocol.getString("payType");

        ReturnObject res = null;
        if (StringUtils.equals("0805", payType)) {// 支付宝
            res = this.aliPayServiceImpl.pay(protocol);
        } else if (StringUtils.equals("0804", payType)) { // 微信
            res = this.wechatServiceImpl.pay(protocol);
        }
        return res;
    }
}



