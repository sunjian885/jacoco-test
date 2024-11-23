package com.test.demo;

import com.alibaba.fastjson.JSONObject;
import com.common.database.DBInfo;
import com.common.database.DBOperate;
import com.common.utils.ConfigProperty;
import com.common.utils.HttpClientUtil;
import com.common.utils.RSAUtil;
import org.json.JSONArray;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ZTJGTest {
    //tenderee-招标人，tenderer-投标人
    static String tendereeAuthorization;
    static String tendererAuthorization;
    static String tokenName = "authorization";
    Random random = new Random();
    int randomNumber = random.nextInt(89999)+10000;
    String projectName = "autoTest"+randomNumber;

    @BeforeTest(description = "获取tenderee-招标人的token")
    public void getLoginToken() throws Exception{
        String userName = ConfigProperty.get("ZTJGUserName");
        String password = ConfigProperty.get("ZTJGPassword");
        String encryptPassword = RSAUtil.encrypt(password);
        //中通建工登录-招标人
        String loginUrl = "http://website.ztzc-test.zszc.jianshicha.cn/api/etbuser/ztkLogin";
        String tendereeBodyData = "{\"username\":\""+userName+"\",\"password\":\""+encryptPassword+"\",\"identity\":\"1\"}";
        //中通建工登录-投标人
        String tendererBodyData = "{\"username\":\"zzlei1\",\"password\":\"ZUaPdIfYvq/y6cielkUYV4OtNM0jnc3VeF7/j_QDkYwEN_GbFp7UVvetAtuXRz5AxAbPFx2Qzc5EccZjiUBOPs8v2Kyn0rZ3bRaA9fMkYg80AKa_BE9_QtKebvoDMv2gXP1wZ3R1wmIBS2U3Tu_i/a36a6N93cnWb1iRgaV0RAU=\",\"identity\":\"1\"}";
        tendereeAuthorization = getUserToken(loginUrl,tendereeBodyData);
        tendererAuthorization = getUserToken(loginUrl,tendererBodyData);
    }

    @DataProvider(name = "testData")
    public Object[][] testData() {
        return new Object[][]{
                {"测试","2"},
                {"科菲",""},
                {"autoTest",""},
                {"","0"},
        };
    }

    @Test(dataProvider = "testData",description = "根据项目名称、审核状态，搜索中标项目")
    public void queryWinningBidProjectTest(String projectName,String status) throws Exception{
        String projectNameEncode = URLEncoder.encode(projectName, "UTF-8");
        String url = "http://trade.ztzc-test.zszc.jianshicha.cn/api/etbtrade-source/winningBidProject/page?size=10&current=1&projectName="+projectNameEncode+"&auditStatus="+status;
        JSONObject getResult = HttpClientUtil.sendGetPairToken(url,tendereeAuthorization,tokenName);
        System.out.println(">>>>>>>>>=getResult "+getResult);
        //TODO 查询数据断言
    }

    @Test(description = "GET请求-招标采购列表")
    public void getTest(){
        //Get请求-采购招标列表
        String getUrl = "http://website.ztzc-test.zszc.jianshicha.cn/api/idr/bulletin/page?current=1&size=12&businessType=2&bulletinType=4%2C13%2C9%2C6%2C12%2C18%2C19&purchaseType=1&searchKey=";
        try {
            JSONObject getResult = HttpClientUtil.sendGetPairToken(getUrl,tendereeAuthorization,tokenName);
            System.out.println("============"+getResult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //todo:断言。连接数据库，比对数据
        //配置文件中对应的数据库链接
        String databaseName = "database.url.jy_biz_open_eval";
        //查询语句
        String selectSql = "SELECT open_start_time,section_id,section_name FROM jy_biz_open_eval.tp_open_section WHERE section_name LIKE \"%1109%\";";

        //断言
        Assert.assertEquals(dbCheck(databaseName,selectSql).get(0).size(),11);
        Assert.assertTrue(true);
    }

    @Test(enabled=false,description = "GET请求-个人中心，招标采购管理-采购计划管理-中标项目列表")
    public void getWinningBidProjectTest(){
        String getWinningBidProjectUrl = "http://trade.ztzc-test.zszc.jianshicha.cn/api/etbtrade-source/winningBidProject/page?size=10&current=1";
        try {
            JSONObject getResult = HttpClientUtil.sendGetPairToken(getWinningBidProjectUrl,tendereeAuthorization,tokenName);
            System.out.println("============"+getResult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //todo:断言。连接数据库，比对数据
    }

    @Test(enabled=false,description = "POST请求-save中标项目")
    public void saveProjectTest(){

        String postUrl = "http://trade.ztzc-test.zszc.jianshicha.cn/api/etbtrade-source/winningBidProject/save?submit=false";
        String bodyData = "{\"area\":\"13,1301,130102\",\"managerIds\":\"1852153743966130176\",\"bidManagerNames\":\"齐东旭\",\"projectName\":\"科菲\",\"addr\":\"神农大厦001\",\"priceYuan\":\"100\",\"fileUrl\":\"ed50c5a9dcc98c9a1236470b65299f0d\",\"price\":10000,\"areaName\":\"河北省/石家庄市/长安区\"}";

        try {
            JSONObject getResult = HttpClientUtil.sendPostBodyToken(postUrl,bodyData,tendereeAuthorization,tokenName);
            System.out.println("============"+getResult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        /*
        todo:断言。连接数据库，比对数据。
         可通过调用接口形式形式获取到项目的id（根据项目名称查询）
         获取到save的项目id，查询数据库，然后断言check。
         */
    }

    @Test(enabled=false,description = "POST请求-submit中标项目")
    public void submitProjectTest(){

        String postUrl = "http://trade.ztzc-test.zszc.jianshicha.cn/api/etbtrade-source/winningBidProject/save?submit=true";
        String bodyData = "{\"area\":\"13,1301,130102\",\"managerIds\":\"1852153743966130176\",\"bidManagerNames\":\"齐东旭\",\"projectName\":\""+projectName+"\",\"addr\":\"神农大厦002\",\"priceYuan\":\"10086\",\"fileUrl\":\"ed50c5a9dcc98c9a1236470b65299f0d\",\"price\":100,\"areaName\":\"河北省/石家庄市/长安区\"}";

        try {
            JSONObject getResult = HttpClientUtil.sendPostBodyToken(postUrl,bodyData,tendereeAuthorization,tokenName);
            System.out.println("============"+getResult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test(enabled=false,description = "PUT请求-处理工单-审核项目-审核通过")
    public void completeTest() throws Exception{
        //todo 通过调用接口或者通过查询数据库，获取到 businessKey，然后进行审核

        //通过接口查询
        String projectNameEncode = URLEncoder.encode(projectName, "UTF-8");
        String url = "http://trade.ztzc-test.zszc.jianshicha.cn/api/etbtrade-source/winningBidProject/page?size=10&current=1&projectName="+projectNameEncode;
        JSONObject getResult = HttpClientUtil.sendGetPairToken(url,tendereeAuthorization,tokenName);
        String list = JSONObject.parseObject(getResult.get("data").toString()).get("list").toString();
        JSONArray jsonArray = new JSONArray(list);
        String businessKey = JSONObject.parseObject(jsonArray.get(0).toString()).get("id").toString();
        //通过businessKey，去审核项目
        String tokenName = "authorization";
        String putUrl = "http://trade.ztzc-test.zszc.jianshicha.cn/api/flowable/task/complete";
        String bodyData = "{\"comment\":\"pass\",\"taskLocalVariables\":{\"approveFiles\":\"59c3f7f4a7e33cca47f62dcc30fe7f6f\"},\"businessKey\":\""+businessKey+"\",\"processVariables\":{}}";

        try {
            String putResult = HttpClientUtil.sendPutBodyToken(putUrl,bodyData,tendereeAuthorization,tokenName);
            System.out.println("============"+putResult);
            //TODO 查询数据库断言、状态是否变更为审核通过
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public static String getUserToken(String loginUrl,String bodyData){
        String token = "";
        try {
            JSONObject result  = HttpClientUtil.sendPostBodyToken(loginUrl,bodyData,"");
            token = JSONObject.parseObject(result.get("data").toString()).get("access_token").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "Bearer "+token;
    }

    public List<Map<String, Object>> dbCheck(String databaseName,String selectSql){
        //数据库查询
        List<Map<String, Object>> selectResult = null;
        try {
            String conn = DBInfo.getConn(databaseName);
            selectResult = DBOperate.select(conn,selectSql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return selectResult;
    }

    public static void main(String[] args) throws Exception{
        //中通建工登录-招标人
        String loginUrl = "http://website.ztzc-test.zszc.jianshicha.cn/api/etbuser/ztkLogin";
        String tendereeBodyData = "{\"username\":\"qidongxu\",\"password\":\"LW_rf3iCSEuPjPMyJ1vT_gMpnQn_OGD72xpr0DPqR6GbZdbXN1Q1ItSCTn/uHO5mhxTOjmfjLOihZlYuIU0UmV19Ddla0kPyB/I7W2LnJO4vqT29WqpHjR7Z14n3o8OnZ2cINrnkHvhlFaiWqneI4MinVcf80CLSUKO33/BQfXY=\",\"identity\":\"1\"}";
        String tendererBodyData = "{\"username\":\"zzlei1\",\"password\":\"ZUaPdIfYvq/y6cielkUYV4OtNM0jnc3VeF7/j_QDkYwEN_GbFp7UVvetAtuXRz5AxAbPFx2Qzc5EccZjiUBOPs8v2Kyn0rZ3bRaA9fMkYg80AKa_BE9_QtKebvoDMv2gXP1wZ3R1wmIBS2U3Tu_i/a36a6N93cnWb1iRgaV0RAU=\",\"identity\":\"1\"}";
        tendereeAuthorization = getUserToken(loginUrl,tendereeBodyData);
        String businessKey;
        //通过接口查询
        String projectNameEncode = URLEncoder.encode("科菲", "UTF-8");
        String url = "http://trade.ztzc-test.zszc.jianshicha.cn/api/etbtrade-source/winningBidProject/page?size=10&current=1&projectName="+projectNameEncode;
        JSONObject getResult = HttpClientUtil.sendGetPairToken(url,tendereeAuthorization,tokenName);
        System.out.println(">>>>>>>>>>>getResult= "+getResult);

        String list = JSONObject.parseObject(getResult.get("data").toString()).get("list").toString();
        System.out.println(">>>>>>>>>>>list= "+list);
        JSONArray jsonArray = new JSONArray(list);
        businessKey = JSONObject.parseObject(jsonArray.get(0).toString()).get("id").toString();
        System.out.println(">>>>>>>>>>>businessKey= "+businessKey);
    }

}
