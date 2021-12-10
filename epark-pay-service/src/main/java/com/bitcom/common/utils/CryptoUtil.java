package com.bitcom.common.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class CryptoUtil {

    /**
     * mdb加密
     * @param str
     * @return
     * @throws Exception
     */
    public static String strToMD5 (String str) throws Exception{
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest  = md5.digest(str.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        String md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;
    }
}
