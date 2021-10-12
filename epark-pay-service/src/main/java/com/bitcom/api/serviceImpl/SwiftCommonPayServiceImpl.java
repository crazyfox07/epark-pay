package com.bitcom.api.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.api.service.ApiGatewayService;
import com.bitcom.common.ReturnObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("swiftCommonPayServiceImpl")
public class SwiftCommonPayServiceImpl
        implements ApiGatewayService {
    @Autowired
    @Qualifier("swiftAliPayServiceImpl")
    private ApiGatewayService aliPayServiceImpl;
    @Autowired
    @Qualifier("swiftWechatPayServiceImpl")
    private ApiGatewayService wechatServiceImpl;
    @Autowired
    @Qualifier("swiftCloudPayServiceImpl")
    private ApiGatewayService swiftCloudPayServiceImpl;

    @Transactional(rollbackFor = {Exception.class})
    public ReturnObject pay(JSONObject protocol) throws Exception {
        String payType = protocol.getString("payType");

        ReturnObject res = null;
        if (StringUtils.equals("0804", payType)) {
            res = this.wechatServiceImpl.pay(protocol);
        } else if (StringUtils.equals("0805", payType)) {
            res = this.aliPayServiceImpl.pay(protocol);
        } else if (StringUtils.equals("0806", payType)) {
            res = this.swiftCloudPayServiceImpl.pay(protocol);
        }
        return res;
    }
}



