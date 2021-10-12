package com.bitcom.sdk.wechat.common;

import org.slf4j.Logger;


public class Log {
    public static final String LOG_TYPE_TRACE = "logTypeTrace";
    public static final String LOG_TYPE_DEBUG = "logTypeDebug";
    public static final String LOG_TYPE_INFO = "logTypeInfo";
    public static final String LOG_TYPE_WARN = "logTypeWarn";
    public static final String LOG_TYPE_ERROR = "logTypeError";
    private Logger logger;

    public Log(Logger log) {
        this.logger = log;
    }


    public void t(String s) {
        this.logger.trace(s);
    }


    public void d(String s) {
        this.logger.debug(s);
    }


    public void i(String s) {
        this.logger.info(s);
    }


    public void w(String s) {
        this.logger.warn(s);
    }


    public void e(String s) {
        this.logger.error(s);
    }


    public void log(String type, String s) {
        if (type.equals("logTypeTrace")) {
            t(s);
        } else if (type.equals("logTypeDebug")) {
            d(s);
        } else if (type.equals("logTypeInfo")) {
            i(s);
        } else if (type.equals("logTypeWarn")) {
            w(s);
        } else if (type.equals("logTypeError")) {
            e(s);
        }
    }
}



