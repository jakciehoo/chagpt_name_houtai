package com.ruoyi.util.weixin;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.ai.service.IconfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WxAppUtilService {
    @Autowired
    private IconfigService iconfigService;
    @Autowired
    private WxCommonUtilService wxCommonUtilService;
    /**
     * 获取密钥sessionkey
     * @param js_code - 小程序登陆后
     */
    public  JSONObject getSessionkey(String js_code) {
        String appid = iconfigService.selectConfigByKey("appid");
        String secret = iconfigService.selectConfigByKey("secret");
        // 利用Hutool工具包的HttpUtil
        Object result = HttpUtil.get("https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code&appid="+appid+"&secret="+secret+"&js_code="+js_code);
        JSONObject entries = JSONUtil.parseObj(result);
        //检查返回文本
        wxCommonUtilService.codeMessageIntroduce(entries);
        return entries;
    }
}
