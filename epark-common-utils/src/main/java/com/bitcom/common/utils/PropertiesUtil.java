package com.bitcom.common.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertiesUtil {
    private static Logger log = LogManager.getLogger(PropertiesUtil.class);

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

    public static void main(String[] args) {
        log.info(PropertiesUtil.getProperty("jedisPass"));
        // System.out.println(PropertiesUtil.getProperty("jedisPass"));
    }
}



