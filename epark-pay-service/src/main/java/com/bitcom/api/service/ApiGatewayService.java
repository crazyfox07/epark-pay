package com.bitcom.api.service;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.common.ReturnObject;

public interface ApiGatewayService {
    ReturnObject pay(JSONObject paramJSONObject) throws Exception;
}



