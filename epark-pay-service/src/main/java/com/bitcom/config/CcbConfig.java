package com.bitcom.config;

/**
 * 建行支付配置
 */
public class CcbConfig {
    public static String ipAddress = "127.0.0.1";
    public static int nPort = 12345;
    public static String MERCHANTID = "105000075235505"; // 商户号
    public static String POSID = "056339629";  //柜台号
    public static String BRANCHID = "370000000";  //分行号
    public static String CcbHost = "https://ibsbjstar.ccb.com.cn/CCBIS/ccbMain";

    public static  String PAY_SCHEME = "0611";
    public static String PAY_TYPE = "0817";
    // 操作员号
    public static String USER_ID = "lxw";
    public static String PASSWORD = "lanneng2020";
}
