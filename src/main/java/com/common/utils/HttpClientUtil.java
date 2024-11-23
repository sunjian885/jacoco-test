package com.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class HttpClientUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final int SUCCESS_CODE = 200;
    private static final int REDIRECT_CODE = 302;
    private static final int SUCCESS_CODE_2 = 201;

    public HttpClientUtil() {
    }

    public static JSONArray sendGet(String url) throws Exception {
        JSONArray jsonObject = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;

        try {
            client = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (200 == statusCode) {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "UTF-8");

                try {
                    jsonObject = JSONObject.parseArray(result);
                    JSONArray var8 = jsonObject;
                    return var8;
                } catch (Exception var14) {
                    Object var9 = null;
                    return (JSONArray)var9;
                }
            }

            LOGGER.error("HttpClientService-line: {}, errorMsg{}", 97, "GET请求失败！");
        } catch (Exception var15) {
            LOGGER.error("HttpClientService-line: {}, Exception: {}", 100, var15);
        } finally {
            if (response != null) {
                response.close();
            }

            client.close();
        }

        return null;
    }

    public static String sendPostPairToken(String url, Map<String, String> otherParams, String token, String tokenName) throws Exception {
        String result = "";
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;

        try {
            List<NameValuePair> params = new ArrayList();
            Iterator var8 = otherParams.entrySet().iterator();

            while(var8.hasNext()) {
                Map.Entry<String, String> map = (Map.Entry)var8.next();
                params.add(new BasicNameValuePair((String)map.getKey(), (String)map.getValue()));
            }

            if (otherParams != null && otherParams.size() > 0) {
                url = url + "?";

                String key;
                for(var8 = otherParams.keySet().iterator(); var8.hasNext(); url = url + key + "=" + (String)otherParams.get(key) + "&") {
                    key = (String)var8.next();
                }

                url = url.substring(0, url.length() - 1);
            }

            HttpPost post = new HttpPost(url);
            post.setEntity(new UrlEncodedFormEntity(params));
            post.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
            post.setHeader(new BasicHeader(tokenName, token));
            CookieStore cookieStore = new BasicCookieStore();
            BasicClientCookie cookie = new BasicClientCookie("sid", token);
            cookie.setVersion(0);
            cookie.setDomain("10.0.91.111");
            cookie.setPath("/");
            cookie.setAttribute("path", "/");
            cookie.setAttribute("domain", "10.0.91.111");
            cookieStore.addCookie(cookie);
            client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            response = client.execute(post);
            if (response != null && response.getStatusLine() != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (200 == statusCode) {
                    result = EntityUtils.toString(response.getEntity(), "UTF-8");
                    String var12 = result;
                    return var12;
                }

                LOGGER.error("HttpClientService-line: {}, errorMsg：{}", 146, "POST请求失败！");
            }
        } catch (Exception var16) {
            LOGGER.error("HttpClientService-line: {}, Exception：{}", 149, var16);
        } finally {
            if (response != null) {
                response.close();
            }

            client.close();
        }

        return result;
    }

    public static JSONObject sendPostBodyToken(String url, String bodyData, String token) throws Exception {
        JSONObject jsonObject = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;

        try {
            client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            StringEntity entity = new StringEntity(bodyData, "UTF-8");
            post.setEntity(entity);
            post.setHeader(new BasicHeader("Content-Type", "application/json"));
            post.setHeader(new BasicHeader("login-token", token));
            response = client.execute(post);
            if (response != null && response.getStatusLine() != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (200 == statusCode) {
                    String result = EntityUtils.toString(response.getEntity(), "UTF-8");

                    try {
                        jsonObject = JSONObject.parseObject(result);
                        JSONObject var10 = jsonObject;
                        return var10;
                    } catch (Exception var16) {
                        Object var11 = jsonObject;
                        return (JSONObject)var11;
                    }
                }

                System.out.println(EntityUtils.toString(response.getEntity(), "UTF-8") + "POST请求失败！");
            }
        } catch (Exception var17) {
            LOGGER.error("HttpClientService-line: {}, Exception：{}", 149, var17);
        } finally {
            if (response != null) {
                response.close();
            }

            client.close();
        }

        return null;
    }

    public static JSONObject sendPostBodyToken(String url, String bodyData, String token, String tokenName) throws Exception {
        JSONObject jsonObject = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;

        try {
            client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            StringEntity entity = new StringEntity(bodyData, "UTF-8");
            post.setEntity(entity);
            post.setHeader(new BasicHeader("Content-Type", "application/json"));
            post.setHeader(new BasicHeader(tokenName, token));
            response = client.execute(post);
            if (response != null && response.getStatusLine() != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (200 == statusCode) {
                    String result = EntityUtils.toString(response.getEntity(), "UTF-8");

                    try {
                        jsonObject = JSONObject.parseObject(result);
                        JSONObject var11 = jsonObject;
                        return var11;
                    } catch (Exception var17) {
                        Object var12 = jsonObject;
                        return (JSONObject)var12;
                    }
                }

                LOGGER.error("HttpClientService-line: {}, errorMsg：{}", 146, "POST请求失败！");
            }
        } catch (Exception var18) {
            LOGGER.error("HttpClientService-line: {}, Exception：{}", 149, var18);
        } finally {
            if (response != null) {
                response.close();
            }

            client.close();
        }

        return null;
    }

    public static String sendPutBodyToken(String url, String bodyData, String token, String tokenName) throws Exception {
        JSONObject jsonObject = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;

        String var10;
        try {
            client = HttpClients.createDefault();
            HttpPut post = new HttpPut(url);
            if (bodyData != null) {
                StringEntity entity = new StringEntity(bodyData, "UTF-8");
                post.setEntity(entity);
            }

            post.setHeader(new BasicHeader("Content-Type", "application/json"));
            post.setHeader(new BasicHeader(tokenName, token));
            response = client.execute(post);
            if (response == null || response.getStatusLine() == null) {
                return null;
            }

            int statusCode = response.getStatusLine().getStatusCode();
            if (200 != statusCode) {
                LOGGER.error("HttpClientService-line: {}, errorMsg：{}", 146, "POST请求失败！");
                return null;
            }

            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            var10 = result;
        } catch (Exception var14) {
            LOGGER.error("HttpClientService-line: {}, Exception：{}", 149, var14);
            return null;
        } finally {
            if (response != null) {
                response.close();
            }

            client.close();
        }

        return var10;
    }

    public static JSONObject sendPutBodyToken(String url, String bodyData, String token) throws Exception {
        JSONObject jsonObject = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;

        try {
            client = HttpClients.createDefault();
            HttpPut post = new HttpPut(url);
            if (bodyData != null) {
                StringEntity entity = new StringEntity(bodyData, "UTF-8");
                post.setEntity(entity);
            }

            post.setHeader(new BasicHeader("Content-Type", "application/json"));
            post.setHeader(new BasicHeader("login-token", token));
            response = client.execute(post);
            if (response != null && response.getStatusLine() != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (200 == statusCode) {
                    String result = EntityUtils.toString(response.getEntity(), "UTF-8");

                    try {
                        jsonObject = JSONObject.parseObject(result);
                        JSONObject var9 = jsonObject;
                        return var9;
                    } catch (Exception var15) {
                        Object var10 = jsonObject;
                        return (JSONObject)var10;
                    }
                }

                LOGGER.error("HttpClientService-line: {}, errorMsg：{}", 146, "POST请求失败！");
            }
        } catch (Exception var16) {
            LOGGER.error("HttpClientService-line: {}, Exception：{}", 149, var16);
        } finally {
            if (response != null) {
                response.close();
            }

            client.close();
        }

        return null;
    }

    public static JSONObject sendPostBodyToken2(String url, String bodyData, String token) throws Exception {
        JSONObject jsonObject = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;

        try {
            client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            StringEntity entity = new StringEntity(bodyData, "UTF-8");
            post.setEntity(entity);
            post.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"));
            if (!token.equals("")) {
                post.setHeader(new BasicHeader("login-token", token));
            }

            response = client.execute(post);
            if (response != null && response.getStatusLine() != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println("statusCode" + statusCode);
                if (201 == statusCode) {
                    String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                    System.out.println("result:" + result);

                    try {
                        jsonObject = JSONObject.parseObject(result);
                        JSONObject var10 = jsonObject;
                        return var10;
                    } catch (Exception var16) {
                        var16.printStackTrace();
                        Object var11 = jsonObject;
                        return (JSONObject)var11;
                    }
                }

                LOGGER.error("HttpClientService-line: {}, errorMsg：{}", 146, "POST请求失败！");
            }
        } catch (Exception var17) {
            LOGGER.error("HttpClientService-line: {}, Exception：{}", 149, var17);
        } finally {
            if (response != null) {
                response.close();
            }

            client.close();
        }

        return null;
    }

    public static String sendPostGetToken(String url, String bodyData, String headName) throws Exception {
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;

        String var9;
        try {
            client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            StringEntity entity = new StringEntity(bodyData, "UTF-8");
            post.setEntity(entity);
            post.setHeader(new BasicHeader("Content-Type", "application/json"));
            response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (200 != statusCode) {
                LOGGER.error("HttpClientService-line: {}, errorMsg：{}", 146, "POST请求失败！");
                return "";
            }

            String result = response.getHeaders(headName)[0].getValue();
            var9 = result;
        } catch (Exception var13) {
            LOGGER.error("HttpClientService-line: {}, Exception：{}", 149, var13);
            return "";
        } finally {
            if (response != null) {
                response.close();
            }

            client.close();
        }

        return var9;
    }

    public static List<String> sendGetCookie(String url, List<NameValuePair> nameValuePairList) throws Exception {
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;

        try {
            client = HttpClients.createDefault();
            URIBuilder uriBuilder = new URIBuilder(url);
            if (nameValuePairList != null) {
                uriBuilder.addParameters(nameValuePairList);
            }

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (200 == statusCode || 302 == statusCode) {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "UTF-8");
                List<String> results = new ArrayList();
                results.add(result);
                Header[] cookies = response.getHeaders("Set-Cookie");
                Header[] var11 = cookies;
                int var12 = cookies.length;

                for(int var13 = 0; var13 < var12; ++var13) {
                    Header c = var11[var13];
                    results.add(c.getValue().split(";")[0]);
                }

                ArrayList var20 = (ArrayList) results;
                return var20;
            }

            LOGGER.error("HttpClientService-line: {}, errorMsg{}", 97, "GET请求失败！");
        } catch (Exception var18) {
            LOGGER.error("HttpClientService-line: {}, Exception: {}", 100, var18);
        } finally {
            if (response != null) {
                response.close();
            }

            client.close();
        }

        return null;
    }

    public static JSONObject sendPostByCookie(String url, List<NameValuePair> nameValuePairList, List<String> cookies) throws Exception {
        JSONObject jsonObject = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;

        try {
            HttpPost post = new HttpPost(url);
            post.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
            String result;
            if (cookies != null) {
                String cookie = "";

                for(Iterator var8 = cookies.iterator(); var8.hasNext(); cookie = cookie + result + ";") {
                    result = (String)var8.next();
                }

                post.setHeader(new BasicHeader("Cookie", cookie));
            }

            client = HttpClients.createDefault();
            post.setEntity(new UrlEncodedFormEntity(nameValuePairList, Consts.UTF_8));
            response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (200 == statusCode) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, "UTF-8");

                try {
                    jsonObject = JSONObject.parseObject(result);
                    JSONObject var10 = jsonObject;
                    return var10;
                } catch (Exception var16) {
                    Object var11 = jsonObject;
                    return (JSONObject)var11;
                }
            }

            LOGGER.error("HttpClientService-line: {}, errorMsg{}", 97, "GET请求失败！");
        } catch (Exception var17) {
            LOGGER.error("HttpClientService-line: {}, Exception: {}", 100, var17);
        } finally {
            if (response != null) {
                response.close();
            }

            client.close();
        }

        return null;
    }

    public static JSONObject sendGetPairToken(String url, String token, String tokenName) throws Exception {
        JSONObject jsonObject = new JSONObject();
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;

        try {
            client = HttpClients.createDefault();
            HttpGet put = new HttpGet(url);
            put.setHeader(new BasicHeader("Content-Type", "application/json"));
            put.setHeader(new BasicHeader(tokenName, token));
            response = client.execute(put);
            if (response != null && response.getStatusLine() != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (200 == statusCode) {
                    String result = EntityUtils.toString(response.getEntity(), "UTF-8");

                    try {
                        jsonObject = JSONObject.parseObject(result);
                        JSONObject var9 = jsonObject;
                        return var9;
                    } catch (Exception var15) {
                        JSONObject var10 = jsonObject;
                        return var10;
                    }
                }

                LOGGER.error("HttpClientService-line: {}, errorMsg：{}", 146, "put请求失败！");
            }
        } catch (Exception var16) {
            LOGGER.error("HttpClientService-line: {}, Exception：{}", 149, var16);
        } finally {
            if (response != null) {
                response.close();
            }

            client.close();
        }

        return jsonObject;
    }

    public static JSONObject sendGetPairToken2(String url, String token) throws Exception {
        JSONObject jsonObject = new JSONObject();
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;

        try {
            client = HttpClients.createDefault();
            HttpGet put = new HttpGet(url);
            put.setHeader(new BasicHeader("Content-Type", "application/json"));
            put.setHeader(new BasicHeader("login-token", token));
            response = client.execute(put);
            if (response != null && response.getStatusLine() != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (200 == statusCode) {
                    String result = EntityUtils.toString(response.getEntity(), "UTF-8").replaceAll("\\$ref", "ref").replaceAll("#/definitions/", "");

                    try {
                        jsonObject = JSONObject.parseObject(result);
                        JSONObject var8 = jsonObject;
                        return var8;
                    } catch (Exception var14) {
                        JSONObject var9 = jsonObject;
                        return var9;
                    }
                }

                LOGGER.error("HttpClientService-line: {}, errorMsg：{}", 146, "put请求失败！");
            }
        } catch (Exception var15) {
            LOGGER.error("HttpClientService-line: {}, Exception：{}", 149, var15);
        } finally {
            if (response != null) {
                response.close();
            }

            client.close();
        }

        return jsonObject;
    }

    public static List<NameValuePair> getParams(Object[] params, Object[] values) {
        boolean flag = params.length > 0 && values.length > 0 && params.length == values.length;
        if (!flag) {
            LOGGER.error("HttpClientService-line: {}, errorMsg：{}", 197, "请求参数为空且参数长度不一致");
            return null;
        } else {
            List<NameValuePair> nameValuePairList = new ArrayList();

            for(int i = 0; i < params.length; ++i) {
                nameValuePairList.add(new BasicNameValuePair(params[i].toString(), values[i].toString()));
            }

            return nameValuePairList;
        }
    }

    public static CookieStore setCookies(Map<String, String> cookies) {
        CookieStore cookieStore = new BasicCookieStore();
        Iterator var2 = cookies.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<String, String> e = (Map.Entry)var2.next();
            List<String> values = Arrays.asList(((String)e.getValue()).split(";"));
            String cookieValue = "";
            String path = "";
            String domain = "";
            Iterator var8 = values.iterator();

            while(var8.hasNext()) {
                String value = (String)var8.next();
                if (value.indexOf((String)e.getKey()) > -1) {
                    cookieValue = value.split("=")[1];
                    System.out.println("cookieValue" + cookieValue);
                } else if (value.indexOf("Path") > -1) {
                    path = value.split("=")[1];
                    System.out.println("path" + path);
                } else if (value.lastIndexOf("Domain") > -1) {
                    domain = value.split("=")[1];
                    System.out.println("domain" + domain);
                }
            }

            BasicClientCookie cookie = new BasicClientCookie((String)e.getKey(), cookieValue);
            cookie.setPath(path);
            cookie.setDomain(domain);
            cookie.setExpiryDate(new Date(System.currentTimeMillis() + 360000000L));
            cookie.setAttribute("path", "/");
            cookieStore.addCookie(cookie);
        }

        return cookieStore;
    }
}
