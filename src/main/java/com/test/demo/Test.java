package com.test.demo;

import com.alibaba.fastjson.JSONObject;
import com.common.database.DBInfo;
import com.common.database.DBOperate;
import com.common.utils.HttpClientUtil;

import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
//        String url = "http://trade.ztzc-test.zszc.jianshicha.cn/api/etbuser/organization/list";
        //中通建工
        String url = "http://trade.ztzc-test.zszc.jianshicha.cn/api/etbtrade-tender/tpPurchasePlan/page?size=10&current=1";
        String tokenName = "authorization";
        String token = "Bearer "+getUserToken();
        try {
            JSONObject result  = HttpClientUtil.sendGetPairToken(url,token,tokenName);
            System.out.println(">>>>>"+result.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //数据库查询
        try {
            String testDBUrl = "database.url.jy_biz_open_eval";
            String selectSql = "SELECT open_start_time,section_id,section_name FROM jy_biz_open_eval.tp_open_section WHERE section_name LIKE \"%1109%\";";
            String conn = DBInfo.getConn(testDBUrl);
            List<Map<String, Object>> selectResult = DBOperate.select(conn,selectSql);
            System.out.println("11111111111111"+selectResult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUserToken(){
        String url = "http://website.ztzc-test.zszc.jianshicha.cn/api/etbuser/ztkLogin";
        String bodyData = "{\"username\":\"qidongxu\",\"password\":\"L/Xh6Jn4jhHuTe3hNM_kO6mlq6Ba5v8v7vsuQIzfVcWXZE0n2/JbAzEE6OdRgG9POV/pdPGwx4vHhLMVV13zXTlcp8jaPoCz1FXiXRKDbB1c9n_MviudCii5r1N8IK/Fw33rkYTujUspEGvbmSegohgj32eIZuoyOUNQrthwQzk=\",\"identity\":\"1\"}";
        String token = "";
        try {
            JSONObject result  = HttpClientUtil.sendPostBodyToken(url,bodyData,"");
            token = JSONObject.parseObject(result.get("data").toString()).get("access_token").toString();
            System.out.println("========"+token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return token;
    }

}
