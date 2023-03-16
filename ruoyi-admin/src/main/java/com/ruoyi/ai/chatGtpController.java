package com.ruoyi.ai;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.chatgpt.domain.TbAnsweEmploy;
import com.ruoyi.chatgpt.domain.TbAnsweUser;
import com.ruoyi.chatgpt.domain.TbKeyManager;
import com.ruoyi.chatgpt.service.ITbAnsweEmployService;
import com.ruoyi.chatgpt.service.ITbAnsweUserService;
import com.ruoyi.chatgpt.service.ITbKeyManagerService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.json.JsonUtil;
import com.ruoyi.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.ruoyi.common.core.domain.AjaxResult.error;
import static com.ruoyi.common.core.domain.AjaxResult.success;

@RequestMapping("/ai")
@RestController
public class chatGtpController {
    @Autowired
    private ITbAnsweEmployService iTbAnsweEmployService;
    @Autowired
    private ISysConfigService iSysConfigService;
    @Autowired
    private WeiXinUtil weiXinUtil;

    @Autowired(required = false)
    private RedisTemplate<Object, Object> redisTemplate;


    @Autowired
    private ITbKeyManagerService iTbKeyManagerService;
    @Autowired
    private ITbAnsweUserService iTbAnsweUserService;

    @PostMapping("/chat")
    public AjaxResult chat(@RequestBody AIVO aivo) {
        //获取是否收录问题
        String is_employ_ask = iSysConfigService.selectConfigByKey("is_employ_ask");
        //是否检查
        String is_check_ask = iSysConfigService.selectConfigByKey("is_check_ask");
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
            String url =  iTbKeyManagerService.getproxyUrl();

            Gpt35TurboVO gpt35TurboVO = new Gpt35TurboVO();
            gpt35TurboVO.setRole("user");
            gpt35TurboVO.setContent(aivo.getPrompt());
            List<Gpt35TurboVO> objects = new ArrayList<>();
            objects.add(gpt35TurboVO);
            Map<Object, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("model", "gpt-3.5-turbo");
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
            String back_ask = iSysConfigService.selectConfigByKey("back_ask");

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

                if (StrUtil.contains(body, "code") && Objects.isNull(JsonUtil.parseMiddleData(body, "code")) && StrUtil.contains(body, "server_error")) {
                    return error("官网爆炸");
                }
            }



            //key用完了次数
            if (StrUtil.contains(body, "code") && Objects.isNull(JsonUtil.parseMiddleData(body, "code")) &&StrUtil.contains(body, "insufficient_quota")) {
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
     * 获取配置信息
     *
     * @return
     */
    @PostMapping("/configInfo")
    public AjaxResult chat() {
        //小程序名称
        String weichat_name = iSysConfigService.selectConfigByKey("weichat_name");
        //小程序广告
        String weichat_adv = iSysConfigService.selectConfigByKey("weichat_adv");
        //小程序公告
        String weichat_notice = iSysConfigService.selectConfigByKey("weichat_notice");
        //是否开启开发方接口
        String is_open_api = iSysConfigService.selectConfigByKey("is_open_api");
        //开发方问答接口
        String ai_chat_api = iSysConfigService.selectConfigByKey("ai_chat_api");
        //开发方聊天接口
        String ai_chat_bot_api = iSysConfigService.selectConfigByKey("ai_chat_bot_api");
        //开发方聊天接口
        String open_api_key = iSysConfigService.selectConfigByKey("open_api_key");

        Map<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("weichat_name", weichat_name);
        objectObjectHashMap.put("weichat_adv", weichat_adv);
        objectObjectHashMap.put("weichat_notice", weichat_notice);
        objectObjectHashMap.put("is_open_api", is_open_api);
        objectObjectHashMap.put("ai_chat_api", ai_chat_api);
        objectObjectHashMap.put("ai_chat_bot_api", ai_chat_bot_api);
        objectObjectHashMap.put("open_api_key", open_api_key);
        return success(objectObjectHashMap);

    }


    /**
     * 插入openid以及另一个信息,初始化个人信息
     *
     * @return
     */
    @PostMapping("/insertOpinID")
    public AjaxResult InsertOpinID(@RequestBody TbAnsweUser tbAnsweUser) {
        if (StrUtil.isBlank(tbAnsweUser.getJs_code())) {
            return error("请求参数为空");
        }
        JSONObject jsonObject = weiXinUtil.getSessionkey(tbAnsweUser.getJs_code());
        String session_key = jsonObject.getStr("session_key");
        String openid = jsonObject.getStr("openid");
        //根据openID去查询,看是否存在该用户
        TbAnsweUser tbAnsweUserSelect = iTbAnsweUserService.selectTbAnsweUserByOpenId(openid);
        if (Objects.isNull(tbAnsweUserSelect)) {
            tbAnsweUserSelect = tbAnsweUser;
            if (StrUtil.isBlank(tbAnsweUserSelect.getAnsweUserName())) {
                return error("用户名为空");
            }
            if (StrUtil.isBlank(tbAnsweUserSelect.getAnsweUserAvatar())) {
                return error("头像为空");
            }
            //初始化回答次数
            tbAnsweUser.setAnsweUserOpenid(openid);
            tbAnsweUserSelect.setAnsweUserNum(0l);
            tbAnsweUserSelect.setAnsweUserBlanceNum(5l);
            tbAnsweUserSelect.setAnsweUserJson("[]");
            int i = iTbAnsweUserService.insertTbAnsweUser(tbAnsweUserSelect);
            if (i == 1) {
                return success(tbAnsweUserSelect);
            }
        }
        return success(tbAnsweUserSelect);
    }


    /**
     * 检查是否回复中
     *
     * @return
     */
    @GetMapping("/checkIsIng")
    public Boolean checkIsIng(String answeUserOpenid) {
        Object o = redisTemplate.opsForValue().get(answeUserOpenid);
        if (Objects.isNull(o)) {
            return false;
        }
        return true;
    }


    @PostMapping("/chatBot")
    public AjaxResult chatBot(@RequestBody TbAnsweUser tbAnsweUser) {

        if (StrUtil.isBlank(tbAnsweUser.getAnsweUserOpenid())) {
            return error("请您先登陆");
        }
        Object o = redisTemplate.opsForValue().get(tbAnsweUser.getAnsweUserOpenid());
        if (!Objects.isNull(o)) {
            return error("正在回复消息");
        }
        redisTemplate.opsForValue().set(tbAnsweUser.getAnsweUserOpenid(), true, 30, TimeUnit.SECONDS);
        TbAnsweUser tbAnsweUserSelect = iTbAnsweUserService.selectTbAnsweUserByOpenId(tbAnsweUser.getAnsweUserOpenid());
        if (Objects.isNull(tbAnsweUserSelect)) {
            redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
            return error("您暂无授权");
        }
        //是否检查
        String is_check_ask = iSysConfigService.selectConfigByKey("is_check_ask");
        if (StrUtil.isBlank(tbAnsweUser.getPrompt())) {
            redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
            return error("输入内容为空");
        }

        if (StrUtil.equals(is_check_ask, "1")) {
            // 通知内容添加文本铭感词汇过滤
            //其余错误见返回码说明
            //正常返回0
            Integer isSensitive = null;
            try {
                isSensitive = weiXinUtil.msgCheck(tbAnsweUser.getPrompt());
            } catch (Exception e) {
                redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
                return error("请重新发送");
            }
            //当图片文件内含有敏感内容，则返回87014
            if (Objects.isNull(isSensitive)) {
                redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
                return error("请重新访问");
            }
            if (isSensitive == 87014) {
                redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
                return error("含有违禁词");
            } else if (isSensitive != 0) {
                redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
                return error("查询出错,请联系管理");
            }
        }
        String body = "";
        List<Gpt35TurboVO> objects = null;
        try {
            AjaxResult ajaxResult = officalGetData(tbAnsweUser);
//                //如果不等于正确的加其他逻辑
            Integer codeR = (Integer) ajaxResult.get("code");
            if (codeR != 200) {
                return ajaxResult;
            }
            String apikey = (String) ajaxResult.get("msg");
            String url =   iTbKeyManagerService.getproxyUrl();

            Gpt35TurboVO gpt35TurboVOSys = new Gpt35TurboVO();
            gpt35TurboVOSys.setRole("system");
            gpt35TurboVOSys.setContent("你是一个可以问答任何问题的全能机器人");

            Gpt35TurboVO gpt35TurboVO = new Gpt35TurboVO();
            gpt35TurboVO.setRole("user");
            gpt35TurboVO.setContent(tbAnsweUser.getPrompt());
            //上下文执行数据
            //首先获取缓存中的List<Gpt35TurboVO>对话

            Object gpt35TurboListObj = redisTemplate.opsForValue().get(tbAnsweUser.getAnsweUserOpenid() + "_chat");
            if (!Objects.isNull(gpt35TurboListObj)) {
                objects = JSON.parseObject(JSON.toJSONString(gpt35TurboListObj), List.class);
                objects.add(gpt35TurboVO);
            } else {
                objects = new ArrayList<>();
                objects.add(gpt35TurboVOSys);
                objects.add(gpt35TurboVO);
            }
            Map<Object, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("model", "gpt-3.5-turbo");
            objectObjectHashMap.put("messages", objects);
            String postData = JSONUtil.toJsonStr(objectObjectHashMap);

            body = HttpRequest.post(url)
                    .header("Authorization", "Bearer " + apikey)//头信息，多个头信息多次调用此方法即可
                    .header("Content-Type", "application/json")
                    .body(postData)//表单内容
                    .timeout(200000)//超时，毫秒
                    .execute().body();
        } catch (Exception e) {
            redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
            return error("请求错误");
        }



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
            if (StrUtil.contains(body, "code") && Objects.isNull(JsonUtil.parseMiddleData(body, "code")) && StrUtil.contains(body, "server_error")) {
                return error("官网爆炸");
            }
        }


        if (StrUtil.isNotBlank(body)) {
            if (StrUtil.contains(body, "statusCode") && StrUtil.contains(body, "TooManyRequests")) {
                redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
                return error("TooManyRequests");
            }
            if (StrUtil.contains(body, "code") && Objects.isNull(JsonUtil.parseMiddleData(body, "code")) &&StrUtil.contains(body, "insufficient_quota")) {

                //首先删除,缓存的key从数据库
                String apikey = (String) redisTemplate.opsForValue().get("apikey");
                if (StrUtil.isNotBlank(apikey)) {
                    iTbKeyManagerService.changeKeyStatusToUsed(apikey);
                }
                //刷新
                redisTemplate.delete("apikey");
                officalGetData(tbAnsweUser);
                redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
                return error("请再次提问,本系统进行了自我维护");
            } else if (StrUtil.contains(JsonUtil.parseMiddleData(body, "model"), "gpt-3.5-turbo")) {
                try {
                    String code = JsonUtil.parseMiddleData(body, "choices");
                    JSONArray jsonArray = JSONUtil.parseArray(code);
                    body = jsonArray.getJSONObject(0).getStr("message");
                    body = JsonUtil.parseMiddleData(body, "content");

                    Gpt35TurboVO gpt35TurboVO = new Gpt35TurboVO();
                    gpt35TurboVO.setRole("assistant");
                    gpt35TurboVO.setContent(body);
                    //上下文执行数据
                    objects.add(gpt35TurboVO);
                    redisTemplate.opsForValue().set(tbAnsweUser.getAnsweUserOpenid() + "_chat", objects, 10, TimeUnit.MINUTES);
                } catch (Exception e) {
                    redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
                    return error("请重新请求");
                }
            } else {
                redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
                return error("我没有搜索出来答案");
            }

            //此处进行对话记录返回
            ReturnAnswerVo returnAnswerVo = new ReturnAnswerVo();
            ContentVo contentVo = new ContentVo();

            //机器人回复
            returnAnswerVo.setFromId(2);
            returnAnswerVo.setAnswerTime(DateTime.now());
            returnAnswerVo.setContent(contentVo);
            //文字回复
            returnAnswerVo.setContentType(0);
            returnAnswerVo.setAnimation(true);
            //查看是否有后缀语
            String back_ask = iSysConfigService.selectConfigByKey("back_ask");
            if (!StrUtil.equals(back_ask, "0")) {
                //添加回复内容
                contentVo.setText(body + "\n" + back_ask);
                redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
                return success(returnAnswerVo);
            }
            contentVo.setText(body);
            returnAnswerVo.setContent(contentVo);
            redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
            return success(returnAnswerVo);
        }
        redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
        return error("请求错误");
    }


    @PostMapping("/reSetChat")
    public AjaxResult reSetChat(String openId) {
        redisTemplate.delete(openId + "_chat");
        return success("重置话题成功");
    }

    @PostMapping("/chatSave")
    public AjaxResult chatSave(@RequestBody TbAnsweUser tbAnsweUser) {

        if (StrUtil.isBlank(tbAnsweUser.getAnsweUserOpenid())) {
            return error("请您先登陆");
        }
        if (StrUtil.isBlank(tbAnsweUser.getAnsweUserJson())) {
            return error("内容为空");
        }
        TbAnsweUser tbAnsweUserSelect = iTbAnsweUserService.selectTbAnsweUserByOpenId(tbAnsweUser.getAnsweUserOpenid());
        if (Objects.isNull(tbAnsweUserSelect)) {
            return error("您暂无授权");
        }
        tbAnsweUserSelect.setAnsweUserJson(tbAnsweUser.getAnsweUserJson());
        int i = iTbAnsweUserService.updateTbAnsweUser(tbAnsweUserSelect);
        if (i == 1) {
            return success("请求成功");
        }
        return error("请求错误");
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
            return error("当前暂无可用的key");
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
            return error("当前暂无可用的key");
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
                if (StrUtil.contains(result2, "code") && Objects.isNull(JsonUtil.parseMiddleData(result2, "code")) &&StrUtil.contains(body, "insufficient_quota")) {
                    redisTemplate.delete("apikey");
                    iTbKeyManagerService.changeKeyStatusToUsed(apikey);
                    officalGetData(tbAnsweUser);
                }else {
//                            return error("我没有搜索出来答案");
                }
            }



        }
    }


    public void changeKeyAsk(TbKeyManager tbKeyManager) {
        String apikey = tbKeyManager.getSecretKey();
        String input = "1加1等于几";
        //请求URL
        String url =   iTbKeyManagerService.getproxyUrl();

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
            if (StrUtil.contains(result2, "code") && Objects.isNull(JsonUtil.parseMiddleData(result2, "code")) && StrUtil.contains(body, "insufficient_quota")) {
                redisTemplate.delete("apikey");
                iTbKeyManagerService.changeKeyStatusToUsed(apikey);
                officalGetDataAsk();
            }else {
//                            return error("我没有搜索出来答案");
            }
        }
    }


}
