package com.bitcom.api.service;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.common.ReturnObject;

public interface IBizScanPaySupport {
    ReturnObject businessScan(JSONObject paramJSONObject) throws Exception;
}



