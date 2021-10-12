package com.bitcom.sdk.wechat;

import com.bitcom.sdk.wechat.util.TenpayUtil;
import com.bitcom.sdk.wechat.util.XmlUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseHandler {
    private Logger log = LoggerFactory.getLogger(getClass());


    private String key;

    private SortedMap parameters;

    private String debugInfo;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private String uriEncoding;

    private Hashtable xmlMap;

    private SortedMap smap;

    private String k;


    public SortedMap getSmap() {
        return this.smap;
    }


    public ResponseHandler(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.smap = new TreeMap();
        this.key = "";
        this.parameters = new TreeMap();
        this.debugInfo = "";

        this.uriEncoding = "";

        try {
            if (request != null) {
                ServletInputStream servletInputStream = request.getInputStream();


                SAXReader reader = new SAXReader();

                reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                Document document = reader.read(servletInputStream);

                Element root = document.getRootElement();

                List<Element> elementList = root.elements();

                for (Element e : elementList) {
                    this.smap.put(e.getName(), e.getText());
                    this.parameters.put(e.getName(), e.getText());
                }
                servletInputStream.close();
                servletInputStream = null;
            }
        } catch (Exception e) {
            this.log.error("抛出异常", e);
        }
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


    public boolean isValidSign() {
        StringBuffer sb = new StringBuffer();
        Set es = this.parameters.entrySet();
        Iterator<Map.Entry> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!"sign".equals(k) && null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }

        sb.append("key=" + getKey());


        String sign = DigestUtils.md5Hex(sb.toString()).toUpperCase();
        String validSign = getParameter("sign");
        return StringUtils.equals(validSign, sign);
    }


    public void sendToCFT(String msg) throws IOException {
        String strHtml = msg;
        PrintWriter out = getHttpServletResponse().getWriter();
        out.println("<xml><return_code>" + strHtml + "</return_code></xml>");
        out.flush();
        out.close();
    }


    public static String success() {
        return "<xml><return_code>SUCCESS</return_code></xml>";
    }


    public static String fail() {
        return "<xml><return_code>FAIL</return_code></xml>";
    }


    public String getUriEncoding() {
        return this.uriEncoding;
    }


    public void setUriEncoding(String uriEncoding) throws UnsupportedEncodingException {
        if (!"".equals(uriEncoding.trim())) {
            this.uriEncoding = uriEncoding;


            String enc = TenpayUtil.getCharacterEncoding(this.request, this.response);
            Iterator<String> it = this.parameters.keySet().iterator();
            while (it.hasNext()) {
                String k = it.next();
                String v = getParameter(k);
                v = new String(v.getBytes(uriEncoding.trim()), enc);
                setParameter(k, v);
            }
        }
    }


    public String getDebugInfo() {
        return this.debugInfo;
    }


    protected void setDebugInfo(String debugInfo) {
        this.debugInfo = debugInfo;
    }


    protected HttpServletRequest getHttpServletRequest() {
        return this.request;
    }


    protected HttpServletResponse getHttpServletResponse() {
        return this.response;
    }
}



