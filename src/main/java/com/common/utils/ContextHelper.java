package com.common.utils;


import com.mysql.cj.util.StringUtils;

public class ContextHelper {
    public ContextHelper() {
    }

    private static String initConfig() {
        String url = "http://weixin.jk.pajk.cn/m.api";
        String serverIP = ConfigProperty.get("serverIP");
        if (!StringUtils.isNullOrEmpty(serverIP)) {
            url = "http://" + serverIP.trim() + "/m.api";
        } else {
            String runmode = ConfigProperty.get("runmode");
            if (!StringUtils.isNullOrEmpty(runmode) && runmode.toLowerCase().equals("test")) {
                url = "http://weixin.pajk.cn/m.api";
            }
        }

        return url;
    }
}
