package com.bitcom.sdk.wechat;

import com.alibaba.fastjson.JSONException;
import com.bitcom.sdk.wechat.client.TenpayHttpClient;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class PrepayIdRequestHandler
        extends RequestHandler {
    public String sendPrepay() throws JSONException {
        String resContent = "";
        StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"GBK\"?><xml>");

        Set es = getAllParameters().entrySet();
        Iterator<Map.Entry> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"appkey".equals(k)) {
                sb.append("<" + k + ">" + v + "</" + k + ">\r\n");
            }
        }
        sb.append("</xml>");
        String params = sb.toString();

        String requestUrl = getGateUrl();

        TenpayHttpClient httpClient = new TenpayHttpClient();
        httpClient.setReqContent(requestUrl);

        if (httpClient.callHttpPost(requestUrl, params)) {
            resContent = httpClient.getResContent();
        }
        return resContent;
    }
}



