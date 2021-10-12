package com.bitcom.sdk.alipay.util;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.bitcom.common.utils.DateUtils;
import com.bitcom.config.AlipayConfig;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

public class AlipayUtil {
    public static String apiGateWay = "https://openapi.alipay.com/gateway.do";

    public static String auth_Url = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=APPID&scope=SCOPE&redirect_uri=ENCODED_URL";


    public static String getOutOrderNo() {
        String timeStr = DateUtils.toTimeStr("yyyyMMddHHmmss");
        Random rad = new Random();
        return timeStr + rad.nextInt(10000);
    }


    public static AlipayClient getAlipayClient() {
        String appId = AlipayConfig.APP_ID;
        String privateKey = AlipayConfig.PRIVATE_KEY_RSA2;
        String aliPublicKey = AlipayConfig.ZFB_PUBLIC_KEY_RSA2;
        return (AlipayClient) new DefaultAlipayClient(apiGateWay, appId, privateKey, "json", "UTF-8", aliPublicKey, "RSA2");
    }


    public static String createGetUrlString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());

        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }


    public static Map<String, String> encodeMapValue(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> result = new HashMap<String, String>(map.size());
        Set<Map.Entry<String, String>> entries = map.entrySet();

        for (Map.Entry<String, String> entry : entries) {
            if (entry.getValue() != null) {
                try {
                    result.put(entry.getKey(), URLEncoder.encode(entry.getValue(), "UTF-8"));
                } catch (Exception exception) {
                }
            }
        }

        return result;
    }


    public static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? (valueStr + values[i]) : (valueStr + values[i] + ",");
            }
            retMap.put(name, valueStr);
        }
        return retMap;
    }
}



