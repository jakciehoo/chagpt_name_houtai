package com.ruoyi.util.sms;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.ruoyi.ai.service.IconfigService;
import com.ruoyi.common.utils.ip.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SmsUtilService {
    @Autowired
    private IconfigService iconfigService;
    @Autowired(required = false)
    private RedisTemplate<String, String> redisTemplate;
//    public static ThreadPoolExecutor executor = new ThreadPoolExecutor(10,200,30, TimeUnit.SECONDS,new LinkedBlockingQueue<>(400),new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 发送验证码
     * @param request
     * @param username
     * @param status
     */
    public void  sendSmsCode(HttpServletRequest request, String username, String status) {
        //阿里云短信模板code
        String aliyun_sms_times_temp = iconfigService.selectConfigByKey("aliyun_sms_times");
        Integer aliyun_sms_times = 60;
        if (StrUtil.isNotBlank(aliyun_sms_times_temp)){
             aliyun_sms_times = Integer.valueOf(aliyun_sms_times_temp);
        }
        String clientIP = IpUtils.getIpAddr_Str(request);
        //作为获取验证码的唯一标识,如果获取一次验证码将IP以及手机号锁定3分钟
//        String preKey = clientIP+"_login_"+phone;
        if (StrUtil.isBlank(username)) {
            throw new RuntimeException("手机号不能为空");
        } else if (!Validator.isMobile(username)) {
            throw new RuntimeException("手机号输入不正确");
        }
        if (StrUtil.isNotBlank(redisTemplate.opsForValue().get(clientIP + "_" + status)) ||
            StrUtil.isNotBlank(redisTemplate.opsForValue().get(clientIP))) {
            Long seconds = redisTemplate.opsForValue().getOperations().getExpire(clientIP + "_" + status);
            throw new RuntimeException("请" + seconds + "秒后重新获取");
        }
        String cmsCode = sendCmsCode(username);
        redisTemplate.opsForValue().set(username + "_" + status + "_username", cmsCode, aliyun_sms_times, TimeUnit.SECONDS);
        //设置IP缓存 - 一个IP 60秒访问一次
        redisTemplate.opsForValue().set(clientIP + "_" + status, cmsCode, aliyun_sms_times, TimeUnit.SECONDS);
    }

    /**
     *  验证码验证
     * @param username
     * @param valismsCode
     * @param status
     */
    public void  checkSmsCode(String username, String valismsCode, String status) {
        if (StrUtil.isBlank(username)) {
            throw new RuntimeException("手机号不可为空");
        }
        if (StrUtil.isBlank(valismsCode)) {
            throw new RuntimeException("验证码不能为空");
        }
        String smsCodeKey = username + "_" + status + "_username";
        Object smsCodeValue = redisTemplate.opsForValue().get(smsCodeKey);
        if (null == smsCodeValue) {
            throw new RuntimeException("请重新获取验证码");
        }
        String smsCode = smsCodeValue.toString();
        if (!StrUtil.equals(smsCode, valismsCode)) {
            throw new RuntimeException("验证码不正确");
        }
        redisTemplate.delete(smsCode);

    }


    public  String sendCmsCode(String username) {
        //阿里云短信accessKeyId
        String aliyun_sms_accessKeyId = iconfigService.selectConfigByKey("aliyun_sms_accessKeyId");
        //阿里云短信secret
        String aliyun_sms_secret = iconfigService.selectConfigByKey("aliyun_sms_secret");
        //阿里云短信签名名称
        String aliyun_sms_sign_name = iconfigService.selectConfigByKey("aliyun_sms_sign_name");
        //阿里云短信模板code
        String aliyun_sms_template_code = iconfigService.selectConfigByKey("aliyun_sms_template_code");
        if (StrUtil.isBlank(aliyun_sms_accessKeyId) || StrUtil.isBlank(aliyun_sms_secret)){
            throw new RuntimeException("请至设置中心填写正确的accessKeyId与secret");
        }
        if (StrUtil.isBlank(aliyun_sms_sign_name) ){
            throw new RuntimeException("请至设置中心填写正确的短信签名名称");
        }
        if (StrUtil.isBlank(aliyun_sms_template_code)){
            throw new RuntimeException("请至设置中心填写正确的短信模板code");
        }
        DefaultProfile profile = DefaultProfile.getProfile("cn-beijing", aliyun_sms_accessKeyId, aliyun_sms_secret);
        /** use STS Token
         DefaultProfile profile = DefaultProfile.getProfile(
         "<your-region-id>",           // The region ID
         "<your-access-key-id>",       // The AccessKey ID of the RAM account
         "<your-access-key-secret>",   // The AccessKey Secret of the RAM account
         "<your-sts-token>");          // STS Token
         **/
        String cmsCode = RandomUtil.randomNumbers(6);
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(username);//接收短信的手机号码
        request.setSignName(aliyun_sms_sign_name);//短信签名名称
        request.setTemplateCode(aliyun_sms_template_code);//短信模板CODE
        request.setTemplateParam("{\"code\":\""+cmsCode+"\"}");//短信模板变量对应的实际值
        try {
            SendSmsResponse response = client.getAcsResponse(request);
            log.info(new Gson().toJson(response));
        } catch (ClientException e) {
            throw  new RuntimeException(e.getErrMsg());
        }
        return cmsCode;
    }
}
