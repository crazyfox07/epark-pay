package com.bitcom.api.service;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.common.ReturnObject;

public interface IOfficialAccountSupport {
    ReturnObject officialAccountPay(JSONObject paramJSONObject) throws Exception;
}



