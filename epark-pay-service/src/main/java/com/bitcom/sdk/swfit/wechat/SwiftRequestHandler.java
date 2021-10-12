package com.bitcom.sdk.swfit.wechat;

import com.alibaba.fastjson.JSONException;
import com.alipay.api.internal.util.AlipaySignature;
import com.bitcom.sdk.wechat.client.TenpayHttpClient;
import com.bitcom.sdk.wechat.util.XmlUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwiftRequestHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());


    private String gateUrl = "https://pay.swiftpass.cn/pay/gateway";


    private SortedMap smap;

    private String key = "";


    private SortedMap<String, Object> parameters = new TreeMap<String, Object>();


    public SwiftRequestHandler(String key) {
        this.key = key;
    }


    public SwiftRequestHandler(HttpServletRequest request) {
        this.smap = new TreeMap();
        this.key = "";
        this.parameters = new TreeMap();

        try {
            if (request != null) {
                ServletInputStream servletInputStream = request.getInputStream();


                SAXReader reader = new SAXReader();

                reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                Document document = reader.read((InputStream) servletInputStream);

                Element root = document.getRootElement();

                List<Element> elementList = root.elements();

                for (Element e : elementList) {
                    this.smap.put(e.getName(), e.getText());
                    this.parameters.put(e.getName(), e.getText());
                }
                servletInputStream.close();
            }
        } catch (Exception e) {
            this.logger.error("抛出异常", e);
        }
    }


    public String getGateUrl() {
        return this.gateUrl;
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


    public void createSign() {
        try {
            String sign = AlipaySignature.rsaSign(getContent(), this.key, "utf-8", "RSA2");
            setParameter("sign", sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validSign() {
        try {
            String content = getContent();
            String validSign = getParameter("sign");
            return AlipaySignature.rsa256CheckContent(content, validSign, this.key, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    private String getContent() {
        List<String> keys = new ArrayList<String>(this.parameters.keySet());
        Collections.sort(keys);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (!"sign".equals(key)) {


                String value = (String) this.parameters.get(key);
                if (i == keys.size() - 1) {
                    sb.append(key).append("=").append(value);
                } else {
                    sb.append(key).append("=").append(value).append("&");
                }
            }
        }
        return sb.toString();
    }

    public void doParse(String xmlContent) throws JDOMException, IOException {
        this.parameters.clear();

        Map m = XmlUtil.doXMLParse(xmlContent);


        Iterator<String> it = m.keySet().iterator();
        while (it.hasNext()) {
            String k = it.next();
            String v = (String) m.get(k);
            setParameter(k, v);
        }
    }

    public String sendRequest() throws JSONException {
        String resContent = "";
        StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"GBK\"?><xml>");
        Set es = getAllParameters().entrySet();
        Iterator<Map.Entry> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v)) {
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



