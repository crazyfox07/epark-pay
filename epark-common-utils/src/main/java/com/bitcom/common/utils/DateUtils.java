package com.bitcom.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class DateUtils {
    public static String toTimeStr(Date date) {
        String format = "yyyy-MM-dd HH:mm:ss";
        return (new SimpleDateFormat(format)).format(date);
    }


    public static String toTimeStr(String format) {
        if (StringUtils.isEmpty(format)) {
            return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
        }
        return (new SimpleDateFormat(format)).format(new Date());
    }


    public static Date parseDate(String value, String format) throws Exception {
        try {
            DateFormat df = new SimpleDateFormat(format);
            return df.parse(value);
        } catch (Exception e) {
            throw e;
        }
    }
}



