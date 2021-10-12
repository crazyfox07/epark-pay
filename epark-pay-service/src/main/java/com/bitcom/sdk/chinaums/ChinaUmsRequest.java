package com.bitcom.sdk.chinaums;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.sdk.wechat.util.WXUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChinaUmsRequest {
    private Logger log = LoggerFactory.getLogger(getClass());

    private String url = "";

    private String appId = "";

    private String appKey = "";

    private String key = "";

    private Map<String, Object> parameters = new HashMap<String, Object>();

    public ChinaUmsRequest() {
    }

    public ChinaUmsRequest(HttpServletRequest request) {
        this.appKey = "";
        this.parameters = new TreeMap();
        try {
            Map requestParams = request.getParameterMap();
            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? (valueStr + values[i]) : (valueStr + values[i] + ",");
                }
                this.parameters.put(name, valueStr);
            }
        } catch (Exception e) {
            this.log.error("抛出异常", e);
        }
    }


    public Map<String, Object> getAllParameters() {
        return this.parameters;
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

    public String sendRequest() throws Exception {
        String timestamp = (String) this.parameters.get("requestTimestamp");
        String nonce = WXUtil.getNonceStr();
        String body = JSONObject.toJSONString(this.parameters);
        String sign = getSignature(timestamp, nonce, body);
        String headers = "OPEN-BODY-SIG AppId=\"" + this.appId + "\", Timestamp=\"" + timestamp + "\", Nonce=\"" + nonce + "\", Signature=\"" + sign + "\"";
        return post(this.url, headers, body);
    }

    public String getSignature(String timestamp, String nonce, String content) throws Exception {
        byte[] data = content.getBytes("utf-8");
        InputStream is = new ByteArrayInputStream(data);
        String sha256HexContent = DigestUtils.sha256Hex(is);
        byte[] hmacSha256 = hmacSHA256((this.appId + timestamp + nonce + sha256HexContent).getBytes("utf-8"), this.appKey.getBytes("utf-8"));
        return Base64.encodeBase64String(hmacSha256);
    }

    private static byte[] hmacSHA256(byte[] data, byte[] key) throws Exception {
        String algorithm = "HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data);
    }

    public static String post(String url, String authorization, String reqBody) {
        String response = "";
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) conn;
            httpUrlConnection.setRequestProperty("Content-Type", "application/json");
            httpUrlConnection.setRequestProperty("authorization", authorization);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setDoInput(true);
            out = new PrintWriter(httpUrlConnection.getOutputStream());
            out.write(reqBody);
            out.flush();
            httpUrlConnection.connect();
            in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                response = response + line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return response;
    }

    public boolean isValidSign() {
        StringBuffer sb = new StringBuffer();
        List<String> keys = new ArrayList<String>(this.parameters.size());
        for (String key : this.parameters.keySet()) {
            if ("sign".equals(key) || "sign_type".equals(key))
                continue;
            if (StringUtils.isEmpty((String) this.parameters.get(key)))
                continue;
            keys.add(key);
        }
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = (String) this.parameters.get(key);
            if (i == keys.size() - 1) {
                sb.append(key + "=" + value);
            } else {
                sb.append(key + "=" + value + "&");
            }
        }
        sb.append(getKey());

        String sign = DigestUtils.md5Hex(sb.toString()).toUpperCase();
        String validSign = getParameter("sign");
        return StringUtils.equals(validSign, sign);
    }


    public String getKey() {
        return this.key;
    }


    public void setKey(String key) {
        this.key = key;
    }


    public String getUrl() {
        return this.url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public String getAppId() {
        return this.appId;
    }


    public void setAppId(String appId) {
        this.appId = appId;
    }


    public String getAppKey() {
        return this.appKey;
    }


    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public static void main(String[] args) throws Exception {
        ChinaUmsRequest bean = new ChinaUmsRequest();
        bean.appId = "10037e6f6a4e6da4016a6284b4de000b";
        bean.appKey = "1eb243a54d1349098af435ae8fac290d";
        String content = "{\"msgId\":\"74486695699844310306879949058208\",\"requestTimestamp\":\"2020-05-12 14:40:25\",\"mid\":\"898340149000035\",\"tid\":\"38557688\",\"instMid\":\"QRPAYDEFAULT\",\"billNo\":\"74443709566967359258989904732078\",\"billDate\":\"2020-05-12 \",\"totalAmount\":10}";
        // String timestamp, String nonce, String content
        String str = bean.getSignature("20200512150053","7989497550336614",content);
        System.out.println(str);


    }
}



