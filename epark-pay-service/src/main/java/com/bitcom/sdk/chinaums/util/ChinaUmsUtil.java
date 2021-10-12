package com.bitcom.sdk.chinaums.util;

import com.bitcom.common.utils.DateUtils;
import com.bitcom.config.ChinaUmsConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;


public class ChinaUmsUtil {
    public static String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }

    public static String getMerOrderId() {
        String msgSrc = ChinaUmsConfig.msgSrcId;
        String dateStr = DateUtils.toTimeStr("yyyyMMddmmHHssSSS");
        String random = getRandomNumber(7);
        return msgSrc + dateStr + random;
    }

    public static String getRandomNumber(int length) {
        String chars = "0123456789";
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sbf.append(chars.charAt((int) (Math.random() * 10.0D)));
        }
        return sbf.toString();
    }

    public static boolean isValidSign(Map<String, Object> map, String key) {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, Object>> es = map.entrySet();
        Iterator<Map.Entry<String, Object>> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);

        String sign = DigestUtils.md5Hex(sb.toString()).toUpperCase();
        return StringUtils.equals((String) map.get("sign"), sign);
    }

    public static void main(String[] args) {
        System.out.println(getTimestamp());
        System.out.println(getRandomNumber(32));
    }
}



