package com.ruoyi.ai.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.ruoyi.ai.doamin.*;
import com.ruoyi.ai.service.IChatGtpService;
import com.ruoyi.ai.service.IconfigService;
import com.ruoyi.chatgpt.domain.TbAnsweEmploy;
import com.ruoyi.chatgpt.domain.TbAnsweUser;
import com.ruoyi.chatgpt.domain.TbKeyManager;
import com.ruoyi.chatgpt.service.ITbAnsweEmployService;
import com.ruoyi.chatgpt.service.ITbAnsweUserService;
import com.ruoyi.chatgpt.service.ITbKeyManagerService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.json.JsonUtil;
import com.ruoyi.util.weixin.WxCommonUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.ruoyi.common.core.domain.AjaxResult.error;
import static com.ruoyi.common.core.domain.AjaxResult.success;




/**
 * GPT流推送
 */
@RequestMapping("/yuan/ai/s/stream")
@RestController
public class ChatGtpSSEStreamController {
    @Autowired
    private ITbAnsweEmployService iTbAnsweEmployService;
    @Autowired
    private IconfigService iconfigService;
    @Autowired
    private IChatGtpService iChatGtpService;
    @Autowired
    private WxCommonUtilService weiXinUtil;
    @Autowired(required = false)
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private ITbKeyManagerService iTbKeyManagerService;
    @Autowired
    private ITbAnsweUserService iTbAnsweUserService;

    @PostMapping("/chat/stream")
    public AjaxResult chatStream(@RequestBody AIVO aivo) {
        //获取是否收录问题
        String is_employ_ask = iconfigService.selectConfigByKey("is_employ_ask");
        //是否检查
        String is_check_ask = iconfigService.selectConfigByKey("is_check_ask");
        if (StrUtil.isBlank(aivo.getPrompt())) {
            return error("输入内容为空");
        }

        if (StrUtil.equals(is_check_ask, "1")) {
            // 通知内容添加文本铭感词汇过滤
            //其余错误见返回码说明
            //正常返回0
            Integer isSensitive = weiXinUtil.msgCheck(aivo.getPrompt());
            //当图片文件内含有敏感内容，则返回87014
            if (Objects.isNull(isSensitive)) {
                return error("请重新访问");
            }
            if (isSensitive == 87014) {
                return error("含有违禁词");
            } else if (isSensitive != 0) {
                return error("查询出错,请联系管理");
            }
        }

        String body = "";
        try {
            AjaxResult ajaxResult = officalGetDataAsk();
            //如果不等于正确的加其他逻辑
            Integer codeR = (Integer) ajaxResult.get("code");
            if (codeR != 200) {
                return ajaxResult;
            }
            String apikey = (String) ajaxResult.get("msg");
            //请求URL
            String url = iTbKeyManagerService.getproxyUrl();

            Gpt35TurboVO gpt35TurboVO = new Gpt35TurboVO();
            gpt35TurboVO.setRole("user");
            gpt35TurboVO.setContent(aivo.getPrompt());
            List<Gpt35TurboVO> objects = new ArrayList<>();
            objects.add(gpt35TurboVO);
            Map<Object, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("model", "gpt-3.5-turbo");
            objectObjectHashMap.put("stream", true);
            objectObjectHashMap.put("temperature", 0);
            objectObjectHashMap.put("messages", objects);
            String postData = JSONUtil.toJsonStr(objectObjectHashMap);

            body = HttpRequest.post(url)
                    .header("Authorization", "Bearer " + apikey)//头信息，多个头信息多次调用此方法即可
                    .header("Content-Type", "application/json")
                    .body(postData)//表单内容
                    .timeout(200000)//超时，毫秒
                    .execute().body();
        } catch (Exception e) {
            return error("请联系开发者");
        }
        if (StrUtil.isNotBlank(body)) {
            //查看是否有后缀语
            String back_ask = iconfigService.selectConfigByKey("back_ask");
            if (StrUtil.isNotBlank(body)) {
                if (StrUtil.contains(body, "invalid_request_error") ) {
                    String error = JsonUtil.parseMiddleData(body, "error");
                    String type = JsonUtil.parseMiddleData(error, "type");
                    String code = JsonUtil.parseMiddleData(error, "code");
                    if (StrUtil.equals(type,"invalid_request_error") && StrUtil.equals(code,"account_deactivated")){
                        return error("账户被封禁");
                    }
                    if (StrUtil.equals(type,"invalid_request_error") && StrUtil.equals(code,"invalid_api_key")) {
                        return error("key不正确");
                    }
                }
                if (StrUtil.contains(body, "statusCode") && StrUtil.contains(body, "TooManyRequests")) {
                    return error("TooManyRequests");
                }

                if (StrUtil.contains(body, "code") && Objects.isNull(JsonUtil.parseMiddleData(body, "code")) &&StrUtil.contains(body, "server_error")) {
                    return error("官网爆炸");
                }
            }


            if (StrUtil.contains(body, "code") && Objects.isNull(JsonUtil.parseMiddleData(body, "code")) && StrUtil.contains(body, "insufficient_quota")) {
                //首先删除,缓存的key从数据库
                String apikey = (String) redisTemplate.opsForValue().get("apikey");
                if (StrUtil.isNotBlank(apikey)) {
                    iTbKeyManagerService.changeKeyStatusToUsed(apikey);
                }
                //刷新
                redisTemplate.delete("apikey");
                officalGetDataAsk();
                return error("请再次提问,本系统进行了自我维护");
            } else if (StrUtil.contains(JsonUtil.parseMiddleData(body, "model"), "gpt-3.5-turbo")) {
                try {
                    String code = JsonUtil.parseMiddleData(body, "choices");
                    JSONArray jsonArray = JSONUtil.parseArray(code);
                    body = jsonArray.getJSONObject(0).getStr("message");
                    body = JsonUtil.parseMiddleData(body, "content");
                } catch (Exception e) {
                    return error("请重新请求");
                }
            } else {
                return error("我没有搜索出来答案");
            }

            if (StrUtil.equals(is_employ_ask, "true")) {
                TbAnsweEmploy tbAnsweEmploy = new TbAnsweEmploy();
                tbAnsweEmploy.setAnserTitle(aivo.getPrompt());
                tbAnsweEmploy.setAnserContent(body);
                int i = iTbAnsweEmployService.insertTbAnsweEmployAuto(tbAnsweEmploy);
                if (i == 1) {
                    if (!StrUtil.equals(back_ask, "0")) {
                        return success(body + "\n" + back_ask);
                    }
                    return success(body);
                } else {
                    return error("请求错误");
                }
            } else {
                if (!StrUtil.equals(back_ask, "0")) {
                    return success(body + "\n" + back_ask);
                }
                return success(body);
            }
        }
        return error("请求错误");
    }



    /**
     * 检查是否回复中
     *
     * @return
     */
    @GetMapping("/checkIsIng")
    public AjaxResult checkIsIng(String userId) {
        Object o = redisTemplate.opsForValue().get(userId);
        return success(!Objects.isNull(o));
    }


    @PostMapping("/chatBot")
    public void chatBot(@RequestBody StreamParametersVO streamParametersVO, HttpServletResponse httpServletResponse) {
        iChatGtpService.chatBotSseStream(streamParametersVO,httpServletResponse);
    }


    /**
     * 小程序获取配置信息
     *
     * @return
     */
    @Anonymous
    @PostMapping("/configInfo")
    public AjaxResult chat() {
        return success(iChatGtpService.getConfigInfo());
    }



    @Anonymous
    @GetMapping("/chatBot1")
    public void chatBo1t(HttpServletResponse httpServletResponse)  {
        try {
            httpServletResponse.setContentType("text/event-stream");
            httpServletResponse.setCharacterEncoding("utf-8");
            PrintWriter pw = httpServletResponse.getWriter();
            int i=0;
            while (i<10){
                TimeUnit.MILLISECONDS.sleep(50);
                //第一段
                pw.write("retry:"+i+"\n");
                pw.flush();
                i++;
            }
            pw.write("data:stop\n\n");
            pw.flush();
        }catch (Exception e){
            System.out.println("11111");
        }
    }


    /**
     * 官方接口获取
     */
    public AjaxResult officalGetData(TbAnsweUser tbAnsweUser) {
        //模仿查到的key集合
        TbKeyManager tbKeyManager = new TbKeyManager();
        tbKeyManager.setIsUse(1);
        //可用的key
        List<TbKeyManager> tbKeyManagers = iTbKeyManagerService.selectTbKeyManagerList(tbKeyManager);
        //判断是否key额度用完
        if (CollectionUtil.isEmpty(tbKeyManagers) || tbKeyManagers.size() <= 0) {
            redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
            return error("key额度已经用完");
        }
        //获取第一个key,然后将第一个key存入缓存
        String key = tbKeyManagers.get(0).getSecretKey();
        redisTemplate.opsForValue().set("apikey", key);
        //检查key
        changeKey(tbAnsweUser,tbKeyManagers.get(0));
        return success(key);
    }


    /**
     * 官方接口获取
     */
    public AjaxResult officalGetDataAsk() {
        //模仿查到的key集合
        TbKeyManager tbKeyManager = new TbKeyManager();
        tbKeyManager.setIsUse(1);
        //可用的key
        List<TbKeyManager> tbKeyManagers = iTbKeyManagerService.selectTbKeyManagerList(tbKeyManager);
        //判断是否key额度用完
        if (CollectionUtil.isEmpty(tbKeyManagers) || tbKeyManagers.size() <= 0) {
            return error("key额度已经用完");
        }
//        //获取第一个key,然后将第一个key存入缓存
        String key = tbKeyManagers.get(0).getSecretKey();
        redisTemplate.opsForValue().set("apikey", key);
        changeKeyAsk(tbKeyManagers.get(0));
        return success(key);
    }


    public void changeKey(TbAnsweUser tbAnsweUser,TbKeyManager tbKeyManager) {
        String apikey = tbKeyManager.getSecretKey();
        String input = "1加1等于几";
        //请求URL
        String url =  iTbKeyManagerService.getproxyUrl();

        Gpt35TurboVO gpt35TurboVO = new Gpt35TurboVO();
        gpt35TurboVO.setRole("user");
        gpt35TurboVO.setContent(input);
        List<Gpt35TurboVO> objects = new ArrayList<>();
        objects.add(gpt35TurboVO);

        Map<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("model", "gpt-3.5-turbo");
        objectObjectHashMap.put("messages", objects);
        String postData = JSONUtil.toJsonStr(objectObjectHashMap);
        String result2 = HttpRequest.post(url)
                .header("Authorization", "Bearer " + apikey)//头信息，多个头信息多次调用此方法即可
                .header("Content-Type", "application/json")
                .body(postData)//表单内容
                .timeout(200000)//超时，毫秒
                .execute().body();
        if (StrUtil.isNotBlank(result2)) {
            String error = JsonUtil.parseMiddleData(result2, "error");
            String type = JsonUtil.parseMiddleData(error, "type");
            if (StrUtil.equals(type, "insufficient_quota")) {
                redisTemplate.delete("apikey");
                iTbKeyManagerService.changeKeyStatusToUsed(apikey);
                officalGetData(tbAnsweUser);
            }
        }




        if (StrUtil.isNotBlank(result2)) {
            if (StrUtil.contains(result2, "invalid_request_error") ) {
                String errorResult = JsonUtil.parseMiddleData(result2, "error");
                String typeResult = JsonUtil.parseMiddleData(errorResult, "type");
                String code = JsonUtil.parseMiddleData(errorResult, "code");
                if (StrUtil.equals(typeResult,"invalid_request_error") && StrUtil.equals(code,"account_deactivated")){
                    redisTemplate.delete("apikey");
                    iTbKeyManagerService.changeKeyStatusToUsed(apikey);
                    officalGetData(tbAnsweUser);
                }
                if (StrUtil.equals(typeResult,"invalid_request_error") && StrUtil.equals(code,"invalid_api_key")) {
                    redisTemplate.delete("apikey");
                    iTbKeyManagerService.changeKeyStatusToUsed(apikey);
                    officalGetData(tbAnsweUser);
                }
            }
            if (StrUtil.contains(result2, "statusCode") && StrUtil.contains(result2, "TooManyRequests")) {
                redisTemplate.delete("apikey");
                officalGetData(tbAnsweUser);
            }
            if (StrUtil.contains(result2, "code") && Objects.isNull(JsonUtil.parseMiddleData(result2, "code")) &&StrUtil.contains(result2, "insufficient_quota")) {
                redisTemplate.delete("apikey");
                iTbKeyManagerService.changeKeyStatusToUsed(apikey);
                officalGetData(tbAnsweUser);
            }else {
//                            return error("我没有搜索出来答案");
            }
        }
    }


    public void changeKeyAsk(TbKeyManager tbKeyManager) {
        String apikey = tbKeyManager.getSecretKey();
        String input = "1加1等于几";
        //请求URL
        String url =  iTbKeyManagerService.getproxyUrl();

        Gpt35TurboVO gpt35TurboVO = new Gpt35TurboVO();
        gpt35TurboVO.setRole("user");
        gpt35TurboVO.setContent(input);
        List<Gpt35TurboVO> objects = new ArrayList<>();
        objects.add(gpt35TurboVO);

        Map<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("model", "gpt-3.5-turbo");
        objectObjectHashMap.put("messages", objects);
        objectObjectHashMap.put("stream", true);
        objectObjectHashMap.put("temperature", 0);
        String postData = JSONUtil.toJsonStr(objectObjectHashMap);
        String result2 = HttpRequest.post(url)
                .header("Authorization", "Bearer " + apikey)//头信息，多个头信息多次调用此方法即可
                .header("Content-Type", "application/json")
                .body(postData)//表单内容
                .timeout(200000)//超时，毫秒
                .execute().body();
        if (StrUtil.isNotBlank(result2)) {
            String error = JsonUtil.parseMiddleData(result2, "error");
            String type = JsonUtil.parseMiddleData(error, "type");
            if (StrUtil.equals(type, "insufficient_quota")) {
                redisTemplate.delete("apikey");
                iTbKeyManagerService.changeKeyStatusToUsed(apikey);
                officalGetDataAsk();
            }
        }

        if (StrUtil.isNotBlank(result2)) {
            if (StrUtil.contains(result2, "invalid_request_error") ) {
                String errorResult = JsonUtil.parseMiddleData(result2, "error");
                String typeResult = JsonUtil.parseMiddleData(errorResult, "type");
                String code = JsonUtil.parseMiddleData(errorResult, "code");
                if (StrUtil.equals(typeResult,"invalid_request_error") && StrUtil.equals(code,"account_deactivated")){
                    redisTemplate.delete("apikey");
                    iTbKeyManagerService.changeKeyStatusToUsed(apikey);
                    officalGetDataAsk();
                }
                if (StrUtil.equals(typeResult,"invalid_request_error") && StrUtil.equals(code,"invalid_api_key")) {
                    redisTemplate.delete("apikey");
                    iTbKeyManagerService.changeKeyStatusToUsed(apikey);
                    officalGetDataAsk();
                }
            }
            if (StrUtil.contains(result2, "statusCode") && StrUtil.contains(result2, "TooManyRequests")) {
                redisTemplate.delete("apikey");
                officalGetDataAsk();
            }
            if (StrUtil.contains(result2, "code") && Objects.isNull(JsonUtil.parseMiddleData(result2, "code")) && StrUtil.contains(result2, "insufficient_quota")) {
                redisTemplate.delete("apikey");
                iTbKeyManagerService.changeKeyStatusToUsed(apikey);
                officalGetDataAsk();
            }else {
//                            return error("我没有搜索出来答案");
            }
        }
    }

    @GetMapping(value = "/test",produces="text/event-stream;charset=UTF-8")
    public void  test(String message){
        //回复用户
        String apikey = "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";
        String input = message;
        //请求URL
        String url =  "https://chatapi.broue.cn/v1/chat/completions";

        Gpt35TurboVO gpt35TurboVO = new Gpt35TurboVO();
        gpt35TurboVO.setRole("user");
        gpt35TurboVO.setContent(input);
        List<Gpt35TurboVO> objects = new ArrayList<>();
        objects.add(gpt35TurboVO);
        Map<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("model", "gpt-3.5-turbo");
        objectObjectHashMap.put("messages", objects);
        objectObjectHashMap.put("stream", true);
        objectObjectHashMap.put("temperature", 0);
        objectObjectHashMap.put("frequency_penalty", 0);
        objectObjectHashMap.put("presence_penalty", 0.6);
        String postData = JSONUtil.toJsonStr(objectObjectHashMap);
        String result2 = HttpRequest.get(url)
                .header("Authorization", "Bearer " + apikey)//头信息，多个头信息多次调用此方法即可
//                .header("Content-Type", "application/json")
//                .header("Accept", "text/event-stream")
                .body(postData)//表单内容
                .timeout(200000)//超时，毫秒
                .execute().body();

        System.out.println(result2);
    }






//
//  public   httpRequestMethon(){
//
//    }





































}
