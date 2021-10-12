package com.bitcom.api.service;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.common.ReturnObject;
import org.apache.commons.lang.StringUtils;


public abstract class AbstractCommonPayService
        implements ApiGatewayService, IAppPaySupport, IBizScanPaySupport, IScanPaySupport, IOfficialAccountSupport {
    public ReturnObject pay(JSONObject protocol) throws Exception {
        String payApiChannel = protocol.getString("payApiChannel");
        JSONObject bizProtocol = protocol.getJSONObject("bizProtocol");

        if (StringUtils.equals(payApiChannel, "1001"))
            return appPay(bizProtocol);
        if (StringUtils.equals(payApiChannel, "1002"))
            return officialAccountPay(bizProtocol);
        if (StringUtils.equals(payApiChannel, "1003")) {
            return businessScan(bizProtocol);
        }
        return ReturnObject.error("payApiChannel=" + payApiChannel + " unsupported.");
    }
}



