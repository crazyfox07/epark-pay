package com.bitcom.common;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


public class ReturnObject
        extends LinkedHashMap<String, Object> {
    public static final String CODE = "code";
    public static final String DATA = "data";
    public static final String MESSAGE = "message";
    private static final long serialVersionUID = 634271777L;

    private ReturnObject() {
        put("code", "0");
        put("message", "操作成功");
    }


    public ReturnObject put(String key, Object value) {
        super.put(key, value);
        return this;
    }


    public static ReturnObject success() {
        return new ReturnObject();
    }


    public static ReturnObject success(String message) {
        ReturnObject r = new ReturnObject();
        if (StringUtils.isNotEmpty(message)) {
            r.put("message", message);
        }
        return r;
    }


    public static ReturnObject success(String message, Object data) {
        ReturnObject r = new ReturnObject();
        if (StringUtils.isNotEmpty(message)) {
            r.put("message", message);
        }
        if (data != null) {
            r.put("data", data);
        }
        return r;
    }


    public static ReturnObject putMap(Map<String, Object> map) {
        ReturnObject r = new ReturnObject();
        r.putAll(map);
        return r;
    }


    public static ReturnObject error() {
        ReturnObject obj = new ReturnObject();
        obj.put("code", "1");
        obj.put("message", "系统错误，请联系系统管理员。");
        return obj;
    }


    public static ReturnObject error(String message) {
        ReturnObject obj = new ReturnObject();
        obj.put("code", "1");
        obj.put("message", message);
        return obj;
    }


    public static ReturnObject error(String code, String message) {
        ReturnObject r = new ReturnObject();
        r.put("code", code);
        r.put("message", message);
        return r;
    }


    public static ReturnObject subError(String errorCode, String errorMessage) {
        ReturnObject returnObj = error();
        Map<String, String> subErrorMap = new HashMap<String, String>();
        subErrorMap.put("errorCode", errorCode);
        subErrorMap.put("errorMessage", errorMessage);
        returnObj.put("error", subErrorMap);
        return returnObj;
    }
}



