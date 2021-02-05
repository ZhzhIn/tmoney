package com.tmoney.foundation.utils;/**
 * @author zhzh.yin
 * @create 2021-02-04 13:56
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 〈配置文件枚举类〉
 *
 * @author zhzh.yin
 * @create 2021/2/4
 */
public enum R {
//    API("api.properties"),


//    TESTDATA("testdata.properties"),

//    EMAIL("email.properties"),

//    REPORT("report.properties"),

    //    DATABASE("database.properties"),
    CONFIG("config.properties");

    private static final Logger LOGGER = Logger.getLogger(R.class);

    private String resourceFile;

    private static Map<String, Properties> propertiesKeeper = new HashMap<String, Properties>();

    static {
        for (R resource : values()) {
            try {
                Properties prop = new Properties();
                prop.load(ClassLoader.getSystemResource(resource.resourceFile).openStream());

                // Ovveride properties
                try {
                    prop.load(ClassLoader.getSystemResource("_" + resource.resourceFile).openStream());
                    LOGGER.info("Properties: " + resource.resourceFile + " were overriden.");
                } catch (Exception e) {
                }
                propertiesKeeper.put(resource.resourceFile, prop);
            } catch (IOException e) {
                LOGGER.error("Properties: " + resource.resourceFile + " not found initialized!");
            }
        }
    }

    R(String resourceKey) {
        this.resourceFile = resourceKey;
    }

    // Will override config property if system property is specified.
    public String get(String key) {
        String sysProperty = System.getProperty(key);
        String cnfgProperty = propertiesKeeper.get(resourceFile).getProperty(key);
        return !StringUtils.isEmpty(sysProperty) ? sysProperty : cnfgProperty;
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public long getLong(String key) {
        return Long.parseLong(get(key));
    }

    public double getDouble(String key) {
        return Double.parseDouble(get(key));
    }

    public static String getResourcePath(String resource) {
        String path = StringUtils.removeStart(ClassLoader.getSystemResource(resource).getPath(), "/");
        path = StringUtils.replaceChars(path, "/", "\\");
        path = StringUtils.replaceChars(path, "!", "");
        return path;
    }
}