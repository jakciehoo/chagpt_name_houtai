package com.ruoyi.quartz.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.quartz.domain.KjRequestTaskParamsVO;
import com.ruoyi.quartz.domain.ReturnSuccessCode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.*;

@Service
public class ProxyIpTaskService {

    /**
     * 用户提取订单号（提取包月版、提取充值版）
     */
    public static String orderId = "O23011221562527687566";
    /**
     * 用户秘钥
     */
    public static String secret = "8e55efdb9b3444bea9100f5d13bc866f";
    /**
     * 提取ip接口链接
     */
    public static String apiUrl = "http://api.hailiangip.com:8422/api/getIp";
    /**
     * 目标url
     */
    private static String destUrl = "https://api.thegod.art/api/goods/goods_detail";

    /**
     * 提取ip协议类型 1:http/https  - get 2:socks5 2:http/https  - post
     */
    public static int type = 3;

    /**
     * 提取个数 min:1  max:200
     */
    public static int num = 1;

    /**
     * 解绑时长
     */
    public static int unbindTime = 60;

    /**
     * 返回数据格式 0:json  1:txt  2:html
     */
    public static int dataType = 1;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    private String proxyHostStr;
    /**
     *
     * @param proxyHostStr
     * @param params
     * @param requestUrl
     * @param requestType
     * @return  返回代码 10001 请求类型不存在   10002 请求代理为空   10003 未知
     * @throws IOException returnSuccessCode
     */
    public ReturnSuccessCode sendRequest(String proxyHostStr,List<NameValuePair> params,String requestUrl,Integer requestType,String headName,String headValue) throws IOException {
        if (!Objects.isNull(proxyHostStr)) {
            ReturnSuccessCode httpResponse = null;
            if (requestType == 1) {
                //使用http协议请求  -  get
                httpResponse = httpGet(proxyHostStr.trim(), requestUrl, headName, headValue);
                //如果返回的不为200则换IP
//                if (httpResponse.getStatusLine().getStatusCode() != 200) {
//                    return httpResponse.getStatusLine().getStatusCode();
//                }
            } else if (requestType == 2) {
                httpResponse = httpPost(proxyHostStr.trim(), requestUrl, params, headName, headValue);
//                if (httpResponse.getStatusLine().getStatusCode() != 200) {
//                    return httpResponse.getStatusLine().getStatusCode();
//                }
                return httpResponse;
            }else {
                return httpResponse;
            }
            return httpResponse;
        }else {
            return null;
        }
    }



    public ReturnSuccessCode  lastMinuteLowPrice(String paramsJson, String requestUrl,Integer requestType,String platformEngLish,String headName,String headValue) throws IOException {
        Map<String,Object> paramsMap =  JSON.parseObject(paramsJson);
        List<NameValuePair> params = new ArrayList<>();
        for (String key:paramsMap.keySet()){
            String value = paramsMap.get(key) + "";
            params.add(new BasicNameValuePair(key, value));
        }
        proxyHostStr = redisTemplate.opsForValue().get("lastMinuteLowPrice_"+platformEngLish);
        if (StrUtil.isBlank(proxyHostStr)){
            proxyHostStr = getProxyHostStr();
            redisTemplate.opsForValue().set("lastMinuteLowPrice_"+platformEngLish,proxyHostStr);
        }
        ReturnSuccessCode returnSuccessCode = sendRequest(proxyHostStr, params, requestUrl, requestType, headName, headValue);
        int statusCode = returnSuccessCode.getCode();
        if (!Objects.isNull(statusCode) && statusCode!=200){
            proxyHostStr = getProxyHostStr();
            redisTemplate.opsForValue().set("lastMinuteLowPrice_"+platformEngLish,proxyHostStr);
        }
        return returnSuccessCode;
    }

    public  String getProxyHostStr() throws IOException {
        //构造api连接地址
        Map<String, Object> params = getParamMap();
        StringBuffer sb = new StringBuffer(apiUrl);
        sb.append("?");
        params.entrySet().stream().forEach(param -> {
            sb.append(param.getKey()).append("=").append(param.getValue()).append("&");
        });
        String url = sb.toString();

        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpGet httpget = new HttpGet(url);
            httpget.addHeader("Accept-Encoding", "gzip");
            CloseableHttpResponse response = httpclient.execute(httpget);
            System.out.println(response.getStatusLine());
            //获取到的结果
            String proxyHostStr = EntityUtils.toString(response.getEntity());
            System.out.println("提取到的代理ip:" + proxyHostStr);
            return proxyHostStr;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }




    public  ReturnSuccessCode httpGet(String proxy_hostname,String destUrl,String headName,String headValue) throws IOException {
        HttpHost proxyHost = HttpHost.create(proxy_hostname);
        CloseableHttpClient client = HttpClients.custom().build();


        HttpGet httpGet = new HttpGet(destUrl);
        if (StrUtil.isNotBlank(headValue) && StrUtil.isNotBlank(headValue) ){
            httpGet.addHeader(headName, headValue);
        }
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(6000)
                .setSocketTimeout(6000).setProxy(proxyHost).build();
        httpGet.setConfig(requestConfig);

        HttpResponse httpResponse = client.execute(httpGet);

        ReturnSuccessCode returnSuccessCode = new ReturnSuccessCode();
        returnSuccessCode.setCode(httpResponse.getStatusLine().getStatusCode());
        if (httpResponse.getStatusLine().getStatusCode()!=200){
            returnSuccessCode.setResponseBody(null);
        }else {
            String resBody = EntityUtils.toString(httpResponse.getEntity());
            /*
             *  将String数组转为JSON
             */
            JSONObject responseBodyObje = new JSONObject(resBody);
            returnSuccessCode.setResponseBody(responseBodyObje);
        }
        System.out.println("http代理请求状态码:" + httpResponse.getStatusLine().getStatusCode());
//        System.out.println("http代理请求响应内容:" + EntityUtils.toString(httpResponse.getEntity()));
        return returnSuccessCode;
    }


    public  ReturnSuccessCode httpPost(String proxy_hostname,String destUrl, List<NameValuePair> params,String headName,String headValue) throws IOException {
        HttpHost proxyHost = HttpHost.create(proxy_hostname);
        CloseableHttpClient client = HttpClients.custom().build();
        HttpPost httpPost = new HttpPost(destUrl);
        if (StrUtil.isNotBlank(headValue) && StrUtil.isNotBlank(headValue) ){
            httpPost.addHeader(headName, headValue);
        }

        httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(6000).setSocketTimeout(6000).setProxy(proxyHost).build();
        httpPost.setConfig(requestConfig);
        HttpResponse httpResponse = client.execute(httpPost);

        ReturnSuccessCode returnSuccessCode = new ReturnSuccessCode();
        returnSuccessCode.setCode(httpResponse.getStatusLine().getStatusCode());
        if (httpResponse.getStatusLine().getStatusCode()!=200){
            returnSuccessCode.setResponseBody(null);
        }else {
            String resBody = EntityUtils.toString(httpResponse.getEntity());
            /*
             *  将String数组转为JSON
             */
            JSONObject responseBodyObje = new JSONObject(resBody);
            returnSuccessCode.setResponseBody(responseBodyObje);
        }

        System.out.println("http代理请求状态码:" + httpResponse.getStatusLine());
//        System.out.println("http代理请求响应内容:" + EntityUtils.toString(httpResponse.getEntity()));
        return returnSuccessCode;
    }

    public  void socks5Get(String proxy_hostname,String destUrl) throws IOException {
        String[] proxyInfo = proxy_hostname.split(":");
        String socks5_proxy_hostname = proxyInfo[0];
        int socks5_proxy_hostport = Integer.parseInt(proxyInfo[1]);
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(socks5_proxy_hostname, socks5_proxy_hostport));
        URL url = new URL(destUrl);
        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection(proxy);
        httpUrlConnection.setRequestMethod("GET");
        httpUrlConnection.setConnectTimeout(5 * 1000);
        httpUrlConnection.setRequestProperty("Accept-Encoding", "gzip");

        // 发起请求
        httpUrlConnection.connect();
        // 输出状态码
        System.out.println("socks5代理请求状态码: " + httpUrlConnection.getResponseCode());
        InputStream inputStream = httpUrlConnection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = reader.readLine()) != null) {
            sb.append(s);
        }
        //响应内容
        System.out.println("socks5代理请求响应内容: " + sb.toString());

        httpUrlConnection.disconnect();

    }

    public  Map<String, Object> getParamMap() {
        /****
         * 详细参数参照产品文档：
         ****/
        Map<String, Object> map = new HashMap(10);
        map.put("type", type);
        map.put("num", num);
        map.put("unbindTime", unbindTime);
        map.put("dataType", dataType);
        map.put("orderId", orderId);
        map.put("time", System.currentTimeMillis() / 1000);
        map.put("sign", getSign(orderId, secret));
        return map;
    }

    public  String getSign(String orderId, String secret) {
        long time = System.currentTimeMillis() / 1000;
        String str1 = String.format("orderId=%s&secret=%s&time=%s", orderId, secret, time);
        String sign = org.apache.commons.codec.digest.DigestUtils.md5Hex(str1).toLowerCase();
        return sign;
    }







}
