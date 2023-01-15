package com.qirsam.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtils {
    private static Properties properties;

    static {
        loadProperties();
    }

    private PropertiesUtils() {

    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static void setProperties(Properties pr) {
        properties = pr;
    }

    private static void loadProperties() {
        if (properties == null) {
            properties = new Properties();
        }
        try (InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
