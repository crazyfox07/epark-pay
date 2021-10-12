package com.bitcom.sdk.wechat;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class ClientRequestHandler
        extends PrepayIdRequestHandler {
    public String getXmlBody() {
        StringBuffer sb = new StringBuffer();
        Set es = getAllParameters().entrySet();
        Iterator<Map.Entry> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!"appkey".equals(k)) {
                sb.append("<" + k + ">" + v + "</" + k + ">\r\n");
            }
        }
        return sb.toString();
    }
}



