package com.bitcom.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class UUIDUtils {
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    /**
     * 获取CCB退款请求序列号，纯数字
     * numLen: 数字位数
     * @return
     */
    public static String getRequestSn(int numLen) {
        int randomNum;
        String resultStr = "";
        for(int i = 0; i < numLen; i++){
            randomNum = (int)(Math.random() * 10);
            resultStr = resultStr + randomNum;
        }
        return resultStr;
    }
}



