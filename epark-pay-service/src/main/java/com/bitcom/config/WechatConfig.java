package com.bitcom.config;


public class WechatConfig {
    public static String PROJECT_NAME = "rzzhtc";


    public static String AppID = "wx0113a07b66706b1a";
    public static String MchID = "1486860082";
    public static String Mch_KEY = "BITCOMBITCOMBITCOMBITCOMBITCOM15";
    public static String CERT_PATH = "/root/cert/app/apiclient_cert.p12";


    public static String appid = "wx123ec7a5388295aa";
    public static String mch_id = "1349899101";
    public static String mch_key = "kgt8ON7yVITDhtdwci0qecXaFptUmEuH";
    public static String cert_path = "/root/cert/js/apiclient_cert.p12";

    static {
        if ("bthtc".equals(PROJECT_NAME)) {
            AppID = "wx9162697f28111b29";
            MchID = "1526998111";
            Mch_KEY = "kgt8ON7yVIEDhtdwci0pecXaFptUmEuH";
            appid = "wx11b08a71bef1c84e";
            mch_id = "1516915221";
            mch_key = "kgt8ON7yVITDhtdwci0pecXaFptUmEuH";
            cert_path = "";
        } else if ("teyiting".equals(PROJECT_NAME)) {
            AppID = "wx2a082fd2fc4f7a5f";
            MchID = "1283022401";
            Mch_KEY = "BITCOMBITCOMBITCOMBITCOMBITCOM15";
            CERT_PATH = "/root/cert/app/apiclient_cert.p12";
            appid = "wxfe25e89948ab597c";
            mch_id = "1262875401";
            mch_key = "kgt8ON7yVITDhtdwci0qecXaFptUmEuH";
            cert_path = "/root/cert/js/apiclient_cert.p12";
        } else if ("NanYang".equals(PROJECT_NAME)) {
            AppID = "wxec139903875ca496";
            MchID = "1523184311";
            Mch_KEY = "kgt8OM7yVITDhtdwc11qecXaFptUmEuc";
            appid = "wx410e3ef3ddd5d21c";
            mch_id = "1513143031";
            mch_key = "d18a61b12872f7bed91cdaf44bd50bc1";
            cert_path = "";
        } else if ("jintan".equals(PROJECT_NAME)) {
            AppID = "wx8d5376706e5d8d72";
            MchID = "1504939181";
            Mch_KEY = "kgt8ON7yVITDhtdwci0qecXaFptUmEuA";
            appid = "wxe381e5cb8969a899";
            mch_id = "1501806621";
            mch_key = "kgt8ON7yVITDhtdwci0qecXaFptUmEuB";
            cert_path = "";
        } else if ("yxbyt".equals(PROJECT_NAME)) {
            AppID = "";
            MchID = "";
            Mch_KEY = "";
            CERT_PATH = "/root/cert/app/apiclient_cert.p12";
            appid = "wx5a7a897f9a21ab4a";
            mch_id = "1552514891";
            mch_key = "kgt8ON7yVITAhtdwci0qecXaFptUmEuH";
            cert_path = "/root/cert/js/apiclient_cert.p12";
        } else if ("rzzhtc".equals(PROJECT_NAME)) {
            AppID = "wx6f09d773927a2518";
            MchID = "1486572862";
            Mch_KEY = "BITCOMBITCOMBITCOMBITCOMBITCOM15";
            CERT_PATH = "/root/cert/app/apiclient_cert.p12";
            appid = "wx9dad87fa3c9c0dce";
            mch_id = "1484493782";
            mch_key = "BITCOMBITCOMBITCOMBITCOMBITCOM15";
            cert_path = "/root/cert/js/apiclient_cert.p12";
        } else if ("offline".equals(PROJECT_NAME)) {
            AppID = "wx1ff09335fb4a86bd";
            MchID = "1581810741";
            Mch_KEY = "882845749296461a873c0627b929d6bb";
            CERT_PATH = "D:/RUN/park-pay/cert/apiclient_cert.p12";
            appid = "wx1ff09335fb4a86bd";
            mch_id = "1581810741";
            mch_key = "882845749296461a873c0627b929d6bb";
            cert_path = "D:/RUN/park-pay/cert/apiclient_cert.p12";
        }
    }
}



