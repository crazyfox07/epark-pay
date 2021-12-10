package com.bitcom.api.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.api.service.ApiGatewayService;
import com.bitcom.common.ReturnObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("commonPayService")
public class CommonPayServiceImpl
        implements ApiGatewayService {
    @Autowired
    @Qualifier("aliPayServiceImpl")
    private ApiGatewayService aliPayServiceImpl;
    @Autowired
    @Qualifier("wechatPayServiceImpl")
    private ApiGatewayService wechatServiceImpl;
    @Autowired
    @Qualifier("swiftCommonPayServiceImpl")
    private ApiGatewayService swiftCommonPayServiceImpl;
    @Autowired
    @Qualifier("chinaUmsCommonPayServiceImpl")
    private ApiGatewayService chinaUmsPayServiceImpl;

    @Autowired
    @Qualifier("ccbPayServiceImpl")
    private  ApiGatewayService ccbPayServiceImpl;

    @Transactional(rollbackFor = {Exception.class})
    public ReturnObject pay(JSONObject protocol) throws Exception {
        String payScheme = protocol.getString("payScheme");
        String payApiChannel = protocol.getString("payApiChannel");
        JSONObject bizProtocol = protocol.getJSONObject("bizProtocol");

        if (StringUtils.isEmpty(payScheme)) {
            return ReturnObject.error("'payScheme' field missing");
        }
        if (StringUtils.isEmpty(payApiChannel)) {
            return ReturnObject.error("'payApiChannel' field missing");
        }
        if (bizProtocol == null) {
            return ReturnObject.error("'bizProtocol' field missing");
        }


        String bizName = bizProtocol.getString("bizName");
        JSONObject params = bizProtocol.getJSONObject("params");
        JSONObject attach = bizProtocol.getJSONObject("attach");

        if (bizName == null) {
            return ReturnObject.error("'bizProtocol.bizName' field missing");
        }
        if (params == null) {
            return ReturnObject.error("'bizProtocol.params' field missing");
        }
        if (attach == null) {
            return ReturnObject.error("'bizProtocol.attach' field missing");
        }

        ReturnObject res = null;
        if (StringUtils.equals("0603", payScheme)) {
            res = this.aliPayServiceImpl.pay(protocol);
        } else if (StringUtils.equals("0602", payScheme)) {
            res = this.wechatServiceImpl.pay(protocol);
        } else if (StringUtils.equals("0604", payScheme)) {
            res = this.swiftCommonPayServiceImpl.pay(protocol);
        } else if (StringUtils.equals("0605", payScheme)) {
            res = this.chinaUmsPayServiceImpl.pay(protocol);
        } else if (StringUtils.equals("0611", payScheme)){
            res = this.ccbPayServiceImpl.pay(protocol);
        }
        return res;
    }
}



