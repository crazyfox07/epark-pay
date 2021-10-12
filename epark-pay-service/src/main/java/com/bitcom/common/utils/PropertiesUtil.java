package com.bitcom.common.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PropertiesUtil {
    private static Logger log = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties properties;

    static {
        InputStream input = PropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties");
        InputStreamReader reader = new InputStreamReader(input);
        properties = new Properties();
        try {
            properties.load(reader);
        } catch (Exception e) {
            log.error("加载配置文件出错", e);
        }
    }


    public static String getProperty(String key) {
        if (properties != null) {
            return properties.getProperty(key).trim();
        }
        return null;
    }
}



