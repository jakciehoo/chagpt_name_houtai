package com.ruoyi.ai;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.ai.service.IconfigService;
import com.ruoyi.common.utils.json.JsonUtil;
import com.ruoyi.system.service.ISysConfigService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tms.v20201229.TmsClient;
import com.tencentcloudapi.tms.v20201229.models.TextModerationRequest;
import com.tencentcloudapi.tms.v20201229.models.TextModerationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@Service
public class WeiXinUtilService {
    @Autowired
    private IconfigService iconfigService;
//    public static final String APPID = "";
//
//    public static final String SECRET = "123456789";

    public static final String AUTHORIZATION_CODE = "zhengshihuangjing";
	public static Map<String, String> accessTokenMap;

    public static final String ACCESS_TOKEN_KEY="ifIRRkhT5Wb1CK";

    @Autowired(required = false)
    private RedisTemplate<Object,Object> redisTemplate;

    /**
     * 功能描述： 获取access_token,每隔7000s获取一次
     * @author jiaoqianjin
     * Date: 2020/11/23 14:33
     */
    @Scheduled(fixedDelay = 2*3000*1000)
    public  void getAccessToken() {


        String appid = iconfigService.selectConfigByKey("appid");
        String secret = iconfigService.selectConfigByKey("secret");
        //利用hutool发送https请求
        Map<String, Object> paramMap = new HashMap<String, Object>(3);
        paramMap.put("appid", appid);
        paramMap.put("grant_type", AUTHORIZATION_CODE);
        paramMap.put("secret", secret);
        // 利用Hutool工具包的HttpUtil
        Object result = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        redisTemplate.opsForValue().set(WeiXinUtilService.ACCESS_TOKEN_KEY,jsonObject.getStr("access_token"),100, TimeUnit.MINUTES);
    }



     /**
     * 功能描述：检查一段文本是否含有违法违规内容。
     *
     * @param content 待检测文本
     * @author jiaoqianjin
     * Date: 2020/11/23 14:41
      * @return 1:标识含有违禁词 2: 重新请求
     */
    public  Integer msgCheck(String content) {
        String token = (String) redisTemplate.opsForValue().get(WeiXinUtilService.ACCESS_TOKEN_KEY);
//        String token = "65_ziCANv9W2kEIe4CxsKyWpA8nQ_61aQw4f29YumI9sh8QyTJtmr45ENL3WV8SEHg8VtlUjE561Q7yLK0VWsqR9UDqpMJG6LQD1uWiBiJlp-Pgh9fKU1OTvrKsTKMXIBaACANYR";
        //如果token为空,则重新获取
        if (StrUtil.isBlank(token)){
            getAccessToken();
            token = (String) redisTemplate.opsForValue().get(WeiXinUtilService.ACCESS_TOKEN_KEY);
        }
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
        paramMap.put("content", content);
        String s = JSONUtil.toJsonStr(paramMap);
//        String s ="{\"content\":\""+content+"\"}";
        String urlStr = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token="+token;

        Object result = HttpRequest
                .post(urlStr)
                .body(s)
                .timeout(5000000)//超时，毫秒
                .execute().body();
        JSONObject jsonObject = JSONUtil.parseObj(result);
        Integer errcode = jsonObject.getInt("errcode");
        if (errcode==40001){
            redisTemplate.delete(WeiXinUtilService.ACCESS_TOKEN_KEY);
        }
        return jsonObject.getInt("errcode");
    }









    /**
     * 功能描述：检查一段文本是否含有违法违规内容。微信
     *
     * @param content 待检测文本
     * @author jiaoqianjin
     * Date: 2020/11/23 14:41
     * @return 1:标识含有违禁词 2: 重新请求
     */
    public String msgCheckWx(String content) {
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential("", "");
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("tms.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            TmsClient client = new TmsClient(cred, "ap-beijing", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            TextModerationRequest req = new TextModerationRequest();
            String CONTENT = Base64.encode(content);
            req.setContent(CONTENT);
            // 返回的resp是一个TextModerationResponse的实例，与请求对象对应
            TextModerationResponse resp = client.TextModeration(req);
            // 输出json格式的字符串回包
            String string = TextModerationResponse.toJsonString(resp);
            return JsonUtil.parseMiddleData(string, "Suggestion");
        } catch (TencentCloudSDKException e) {
            return "error";
        }
    }

    /**
     * 获取密钥sessionkey
     * @param js_code - 小程序登陆后获取的
     */
    public  JSONObject getSessionkey(String js_code) {
        String appid = iconfigService.selectConfigByKey("appid");
        String secret = iconfigService.selectConfigByKey("secret");
        // 利用Hutool工具包的HttpUtil
        Object result = HttpUtil.get("https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code&appid="+appid+"&secret="+secret+"&js_code="+js_code);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        return jsonObject;
    }
}

