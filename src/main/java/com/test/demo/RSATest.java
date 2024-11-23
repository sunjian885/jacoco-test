package com.test.demo;

import com.alibaba.fastjson.JSONObject;
import com.common.utils.ConfigProperty;
import com.common.utils.HttpClientUtil;
import com.common.utils.RSAUtil;

public class RSATest {
    public static void main(String[] args) throws Exception{
        String userName = ConfigProperty.get("ZTJGUserName");
        String password = ConfigProperty.get("ZTJGPassword");
        String encryptPassword = RSAUtil.encrypt(password);
        System.out.println(">>>>userName="+userName);
        System.out.println(">>>>password="+password);
        System.out.println(">>>>encryptPassword="+encryptPassword);
        String loginUrl = "http://website.ztzc-test.zszc.jianshicha.cn/api/etbuser/ztkLogin";
        String bodyData = "{\"username\":\""+userName+"\",\"password\":\""+encryptPassword+"\",\"identity\":\"1\"}";
        JSONObject result  = HttpClientUtil.sendPostBodyToken(loginUrl,bodyData,"");
        String token = JSONObject.parseObject(result.get("data").toString()).get("access_token").toString();
        System.out.println("==========token= "+token);
    }

}
