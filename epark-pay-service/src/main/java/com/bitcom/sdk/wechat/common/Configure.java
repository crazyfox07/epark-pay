package com.bitcom.sdk.wechat.common;


public class Configure {
    private static String key = "";


    private static String appID = "";


    private static String mchID = "";


    private static String subMchID = "";


    private static String certLocalPath = "";


    private static String certPassword = "";


    private static boolean useThreadToDoReport = true;


    private static String ip = "";


    public static String PAY_API = "https://api.mch.weixin.qq.com/pay/micropay";


    public static String PAY_QUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";


    public static String REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";


    public static String REFUND_QUERY_API = "https://api.mch.weixin.qq.com/pay/refundquery";


    public static String REVERSE_API = "https://api.mch.weixin.qq.com/secapi/pay/reverse";


    public static String DOWNLOAD_BILL_API = "https://api.mch.weixin.qq.com/pay/downloadbill";


    public static String REPORT_API = "https://api.mch.weixin.qq.com/payitil/report";


    public static boolean isUseThreadToDoReport() {
        return useThreadToDoReport;
    }


    public static void setUseThreadToDoReport(boolean useThreadToDoReport) {
        Configure.useThreadToDoReport = useThreadToDoReport;
    }


    public static String HttpsRequestClassName = "com.bitcom.sdk.wechat.common.HttpsRequest";


    public static void setKey(String key) {
        Configure.key = key;
    }


    public static void setAppID(String appID) {
        Configure.appID = appID;
    }


    public static void setMchID(String mchID) {
        Configure.mchID = mchID;
    }


    public static void setSubMchID(String subMchID) {
        Configure.subMchID = subMchID;
    }


    public static void setCertLocalPath(String certLocalPath) {
        Configure.certLocalPath = certLocalPath;
    }


    public static void setCertPassword(String certPassword) {
        Configure.certPassword = certPassword;
    }


    public static void setIp(String ip) {
        Configure.ip = ip;
    }


    public static String getKey() {
        return key;
    }


    public static String getAppid() {
        return appID;
    }


    public static String getMchid() {
        return mchID;
    }


    public static String getSubMchid() {
        return subMchID;
    }


    public static String getCertLocalPath() {
        return certLocalPath;
    }


    public static String getCertPassword() {
        return certPassword;
    }


    public static String getIP() {
        return ip;
    }


    public static void setHttpsRequestClassName(String name) {
        HttpsRequestClassName = name;
    }
}



