package com.bitcom.sdk.wechat;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;

public class RequestHandler {
    private String gateUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";


    private String key = "";


    private SortedMap parameters = new TreeMap();


    private String debugInfo = "";


    public void init() {
    }


    public String getGateUrl() {
        return this.gateUrl;
    }


    public void setGateUrl(String gateUrl) {
        this.gateUrl = gateUrl;
    }


    public String getKey() {
        return this.key;
    }


    public void setKey(String key) {
        this.key = key;
    }


    public String getParameter(String parameter) {
        String s = (String) this.parameters.get(parameter);
        return (null == s) ? "" : s;
    }


    public void setParameter(String parameter, String parameterValue) {
        String v = "";
        if (null != parameterValue) {
            v = parameterValue.trim();
        }
        this.parameters.put(parameter, v);
    }


    public SortedMap getAllParameters() {
        return this.parameters;
    }


    public String getDebugInfo() {
        return this.debugInfo;
    }


    public void createSign() {
        StringBuffer sb = new StringBuffer();
        Set es = this.parameters.entrySet();
        Iterator<Map.Entry> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) &&
                    !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + getKey());

        String sign = DigestUtils.md5Hex(sb.toString()).toUpperCase();

        setParameter("sign", sign);


        setDebugInfo(sb.toString() + " => sign:" + sign);
    }


    protected void setDebugInfo(String debugInfo) {
        this.debugInfo = debugInfo;
    }
}



