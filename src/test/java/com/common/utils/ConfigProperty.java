package com.common.utils;


import com.mysql.cj.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ConfigProperty {
    private static Map<String, String> propMap = new HashMap();

    public ConfigProperty() {
    }

    public static String get(String key) {
        if (!StringUtils.isNullOrEmpty(System.getProperty("Config.runmode"))) {
            propMap.put("runmode", System.getProperty("Config.runmode"));
        }

        if (!StringUtils.isNullOrEmpty(System.getProperty("Config.serverIP"))) {
            propMap.put("serverIP", System.getProperty("Config.serverIP"));
        }

        return (String)propMap.get(key);
    }

    static {
        InputStream input = ConfigProperty.class.getResourceAsStream("/application.properties");
        Properties prop = new Properties();
        if (input != null) {
            try {
                prop.load(input);
                Set<Object> keyset = prop.keySet();
                Iterator var3 = keyset.iterator();

                while(var3.hasNext()) {
                    Object object = var3.next();
                    String propKey = object.toString().trim();
                    String propValue = prop.getProperty(propKey).toString().trim();
                    propMap.put(propKey, propValue);
                    System.out.println(propKey + ":" + propValue);
                }
            } catch (IOException var34) {
                var34.printStackTrace();
            } finally {
                try {
                    input.close();
                } catch (IOException var31) {
                    var31.printStackTrace();
                }

            }
        }

        if (propMap.containsKey("runmode")) {
            String configFilePath = "/config/application.properties." + ((String)propMap.get("runmode")).toLowerCase();
            InputStream input2 = ContextHelper.class.getResourceAsStream(configFilePath);
            Properties prop2 = new Properties();
            if (input2 != null) {
                try {
                    prop2.load(input2);
                    Set<Object> keyset = prop2.keySet();
                    Iterator var40 = keyset.iterator();

                    while(var40.hasNext()) {
                        Object object = var40.next();
                        String propKey = object.toString().trim();
                        String propValue = prop2.getProperty(propKey).toString().trim();
                        propMap.put(propKey, propValue);
                        System.out.println(propKey + ":" + propValue);
                    }
                } catch (IOException var32) {
                    var32.printStackTrace();
                } finally {
                    try {
                        input2.close();
                    } catch (IOException var30) {
                        var30.printStackTrace();
                    }

                }
            }
        }

    }
}
