package com.bitcom.sdk.wechat.common;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


public class Signature {
    public static String getSign(Object o) throws IllegalAccessException {
        ArrayList<String> list = new ArrayList<String>();
        Class<?> cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.get(o) != null && f.get(o) != "") {
                list.add(f.getName() + "=" + f.get(o) + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result = result + "key=" + Configure.getKey();
        Util.log("Sign Before MD5:" + result);
        result = MD5.MD5Encode(result).toUpperCase();
        Util.log("Sign Result:" + result);
        return result;
    }

    public static String getSign(Map<String, Object> map) {
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != "") {
                list.add((String) entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result = result + "key=" + Configure.getKey();

        result = MD5.MD5Encode(result).toUpperCase();

        return result;
    }


    public static String getSignFromResponseString(String responseString) throws IOException, SAXException, ParserConfigurationException {
        Map<String, Object> map = XMLParser.getMapFromXML(responseString);

        map.put("sign", "");

        return getSign(map);
    }


    public static boolean checkIsSignValidFromResponseString(String responseString) throws ParserConfigurationException, IOException, SAXException {
        Map<String, Object> map = XMLParser.getMapFromXML(responseString);
        Util.log(map.toString());

        String signFromAPIResponse = map.get("sign").toString();
        if (signFromAPIResponse == "" || signFromAPIResponse == null) {
            Util.log("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
            return false;
        }
        Util.log("服务器回包里面的签名是:" + signFromAPIResponse);

        map.put("sign", "");

        String signForAPIResponse = getSign(map);

        if (!signForAPIResponse.equals(signFromAPIResponse)) {

            Util.log("API返回的数据签名验证不通过，有可能被第三方篡改!!!");
            return false;
        }
        Util.log("恭喜，API返回的数据签名验证通过!!!");
        return true;
    }
}



