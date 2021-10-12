package com.bitcom.sdk.wechat.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;


public class WXUtil {
    public static String getNonceStr() {
        Random random = new Random();
        return DigestUtils.md5Hex(String.valueOf(random.nextInt(10000)));
    }


    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000L);
    }


    public static String createOutTradeNo() {
        String currTime = getCurrTime();

        String strRandom = buildRandom(4) + "";

        String strReq = currTime + strRandom;

        String out_trade_no = strReq;

        return out_trade_no;
    }


    public static String toString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }


    public static int toInt(Object obj) {
        int a = 0;
        try {
            if (obj != null)
                a = Integer.parseInt(obj.toString());
        } catch (Exception exception) {
        }


        return a;
    }


    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }


    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String strDate = formatter.format(date);
        return strDate;
    }


    public static int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1D) {
            random += 0.1D;
        }
        for (int i = 0; i < length; i++) {
            num *= 10;
        }
        return (int) (random * num);
    }


    public static String getCharacterEncoding(HttpServletRequest request, HttpServletResponse response) {
        if (null == request || null == response) {
            return "gbk";
        }

        String enc = request.getCharacterEncoding();
        if (null == enc || "".equals(enc)) {
            enc = response.getCharacterEncoding();
        }

        if (null == enc || "".equals(enc)) {
            enc = "gbk";
        }

        return enc;
    }


    public static long getUnixTime(Date date) {
        if (null == date) {
            return 0L;
        }

        return date.getTime() / 1000L;
    }


    public static String date2String(Date date, String formatType) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        return sdf.format(date);
    }
}



