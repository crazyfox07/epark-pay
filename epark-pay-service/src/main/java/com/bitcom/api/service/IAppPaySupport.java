package com.bitcom.api.service;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.common.ReturnObject;

public interface IAppPaySupport {
    ReturnObject appPay(JSONObject paramJSONObject) throws Exception;
}



