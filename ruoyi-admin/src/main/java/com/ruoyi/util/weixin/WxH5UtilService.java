package com.ruoyi.util.weixin;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.ai.service.IconfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class WxH5UtilService {
    @Autowired
    private IconfigService iconfigService;
    @Autowired(required = false)
    private RedisTemplate<Object,Object> redisTemplate;
    @Autowired
    private WxCommonUtilService wxCommonUtilService;
    /**
     * 微信公众号token
     */
    public static final String WX_H5_TOKEN_ACCESS_TOKEN = "33jjdsk";
    /**
     * 微信公众号token
     */
    public static final String WX_H5_TOKEN_REFRESH_TOKEN = "qqedv444gfdhgdsfeww";

    /**
     * 获取Access_Tonken     --- 微信h5
     * @param js_code
     */
    public  String  wxH5OpenId(String js_code) {
        //获取token
        String WX_H5_TOKEN_ACCESS_TOKEN = (String) redisTemplate.opsForValue().get(WxH5UtilService.WX_H5_TOKEN_ACCESS_TOKEN);
        String WX_H5_TOKEN_REFRESH_TOKEN = (String) redisTemplate.opsForValue().get(WxH5UtilService.WX_H5_TOKEN_REFRESH_TOKEN);
        //token为空，刷新token不为空
        if(StrUtil.isBlank(WX_H5_TOKEN_ACCESS_TOKEN) && StrUtil.isBlank(WX_H5_TOKEN_REFRESH_TOKEN)){
            return this.wxH5AccessTonken(js_code);
        }else {
            return  this.wxH5RefreshAccessTonken(WX_H5_TOKEN_REFRESH_TOKEN);
        }
    }

    /**
     * 获取Refersh_Tonken  --- 微信h5
     * @param js_code
     */
    public  String  wxH5AccessTonken(String js_code) {
        String appid = iconfigService.selectConfigByKey("wx_h5_appid");
        String secret = iconfigService.selectConfigByKey("wx_h5_secret");
        if (StrUtil.isBlank(appid) || StrUtil.isBlank(secret)){
            throw new RuntimeException("公众号的appid以及secret请至后台填写");
        }
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+js_code+"&grant_type=authorization_code";
        String body = HttpRequest.get(url).execute().body();
        JSONObject jsonObject = JSONUtil.parseObj(body);
        //检查返回文本
        wxCommonUtilService.codeMessageIntroduce(jsonObject);
        String access_token = jsonObject.getStr("access_token");
        String refresh_token = jsonObject.getStr("refresh_token");
        redisTemplate.opsForValue().set(WxH5UtilService.WX_H5_TOKEN_ACCESS_TOKEN,access_token,100, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(WxH5UtilService.WX_H5_TOKEN_REFRESH_TOKEN,refresh_token,29, TimeUnit.DAYS);
        return jsonObject.getStr("openid");
    }
    /**
     * 获取Refersh_Tonken  --- 微信h5
     * @param refresh_token
     */
    public  String  wxH5RefreshAccessTonken(String refresh_token) {
        String appid = iconfigService.selectConfigByKey("wx_h5_appid");
        String secret = iconfigService.selectConfigByKey("wx_h5_secret");
        if (StrUtil.isBlank(appid) || StrUtil.isBlank(secret)){
            throw new RuntimeException("公众号的appid以及secret请至后台填写");
        }
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="+appid+"&grant_type=refresh_token&refresh_token="+refresh_token;
        String body = HttpRequest.get(url).execute().body();
        JSONObject jsonObject = JSONUtil.parseObj(body);
        //检查返回文本
        wxCommonUtilService.codeMessageIntroduce(jsonObject);
        String access_token = jsonObject.getStr("access_token");
        redisTemplate.opsForValue().set(WxH5UtilService.WX_H5_TOKEN_ACCESS_TOKEN,access_token,100, TimeUnit.MINUTES);
        return jsonObject.getStr("openid");
    }
    /**
     * 获取H5的用户信息
     */
    public  JSONObject  wxH5InfoMation(String js_code) {
        //刷新token-需要时会刷新
        String openId = this.wxH5OpenId(js_code);
        String WX_H5_TOKEN_ACCESS_TOKEN = (String) redisTemplate.opsForValue().get(WxH5UtilService.WX_H5_TOKEN_ACCESS_TOKEN);
        // 利用Hutool工具包的HttpUtil
        String  url = "https://api.weixin.qq.com/sns/userinfo?access_token="+WX_H5_TOKEN_ACCESS_TOKEN+"&openid="+openId+"&lang=zh_CN";
        String body = HttpRequest.get(url).execute().body();
        JSONObject jsonObject = JSONUtil.parseObj(body);
        //检查返回文本
        wxCommonUtilService.codeMessageIntroduce(jsonObject);
        return jsonObject;
    }
}
