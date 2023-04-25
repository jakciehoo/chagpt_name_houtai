package com.ruoyi.login;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class wxlongmain {
    public static void main(String[] args) {
        String appid = "wx2a8e7f8ff2d4a196";
        String secret = "8dba087c6d490f0bafe17d69234a0674";
        String str = "{\"access_token\":\"67_Oiu85wjt_ppdy2Dflkw7AIvpNDFATdL5Vaw8oe8OP0iQFjeQoEKiY1bwyupfLjf8cNnPdx2526xWen7BcFB64KcEj3j0k2m3MsRecArAAhc\",\"expires_in\":7200,\"refresh_token\":\"67_RJuNRIAR3MBdfTbxErIb_faZbzWcvkRf8nmm1eilK7rjlnMfctHEtr9r24Cycx_nvG9FQz3d6tCj_vVliu55r0GB6u6TH0hFBD_QQoGYuWE\",\"openid\":\"ooVXu6nrfSsHpOI14XDG4ypjZqPQ\",\"scope\":\"snsapi_userinfo\"}";

        JSONObject jsonObject = JSONUtil.parseObj(str);
        String access_token = jsonObject.getStr("access_token");
        String refresh_token = jsonObject.getStr("refresh_token");
        String openid  = jsonObject.getStr("openid");


//        JSONObject jsonObject = JSONUtil.parseObj(body);
//        String access_token = jsonObject.getStr("access_token");
//        String openid  = jsonObject.getStr("openid");
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        String body = HttpRequest.get(url).execute().body();

        System.out.println("111");
    }
}
