package com.ruoyi.ai;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.chatgpt.domain.TbAnsweEmploy;
import com.ruoyi.chatgpt.domain.TbAnsweUser;
import com.ruoyi.chatgpt.service.ITbAnsweEmployService;
import com.ruoyi.chatgpt.service.ITbAnsweUserService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.json.JsonUtil;
import com.ruoyi.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private ITbAnsweUserService iTbAnsweUserService;
    @PostMapping("/chat")
    public AjaxResult chat(@RequestBody  AIVO aivo) {
        //获取是否收录问题
        String is_employ_ask = iSysConfigService.selectConfigByKey("is_employ_ask");
        //是否检查
        String is_check_ask = iSysConfigService.selectConfigByKey("is_check_ask");
        if (StrUtil.isBlank(aivo.getPrompt())){
          return   error("输入内容为空");
        }

        if (StrUtil.equals(is_check_ask,"1")){
            // 通知内容添加文本铭感词汇过滤
            //其余错误见返回码说明
            //正常返回0
            Integer isSensitive = weiXinUtil.msgCheck(aivo.getPrompt());
            //当图片文件内含有敏感内容，则返回87014
            if (Objects.isNull(isSensitive)){
                return error("请重新访问");
            }
            if (isSensitive==87014) {
                return error("含有违禁词");
            }else if (isSensitive!=0){
                return error("查询出错,请联系管理");
            }
        }

        String body = "";
        //获取数据方式:1表示获取官方数据,2获取接口转发数据
        String get_data_type = iSysConfigService.selectConfigByKey("get_data_type");


        try {
            if (StrUtil.equals(get_data_type,"2")){
                //暂无逻辑
            }else {
                AjaxResult ajaxResult = officalGetDataAsk();
                //如果不等于正确的加其他逻辑
                Integer codeR =(Integer) ajaxResult.get("code");
                if (codeR!=200){
                    return ajaxResult;
                }
//            ajaxResult.get
                String apikey =(String)ajaxResult.get("data");
                //请求URL
                String  url = "https://api.openai.com/v1/completions";
                String [] a = new String[]{"\n"};
                Map<Object, Object> objectObjectHashMap = new HashMap<>();
                objectObjectHashMap.put("model","text-davinci-003");
                objectObjectHashMap.put("prompt",aivo.getPrompt());
                objectObjectHashMap.put("max_tokens",2048);
                String postData = JSONUtil.toJsonStr(objectObjectHashMap);
                body = HttpRequest.post(url)
                        .header("Authorization", "Bearer "+apikey)//头信息，多个头信息多次调用此方法即可
                        .header("Content-Type","application/json")
                        .body(postData)//表单内容
                        .timeout(200000)//超时，毫秒
                        .execute().body();
            }
        }catch (Exception e){
            return error("请重新资讯");
        }

        if (StrUtil.isNotBlank(body)){
            //查看是否有后缀语
            String back_ask = iSysConfigService.selectConfigByKey("back_ask");
            if (StrUtil.contains(body,"statusCode") && StrUtil.contains(body,"TooManyRequests")){
                return error("请求错误");
            }
            if (StrUtil.contains(body,"code") && Objects.isNull(JsonUtil.parseMiddleData(body, "code"))){
                //刷新
                redisTemplate.delete("apikey");
                officalGetDataAsk();
                return error("请重新发送");
            }else if (StrUtil.equals(JsonUtil.parseMiddleData(body, "model"),"text-davinci-003")){
                try {
                    String code = JsonUtil.parseMiddleData(body, "choices");
                    JSONArray jsonArray = JSONUtil.parseArray(code);
                    body = jsonArray.getJSONObject(0).getStr("text");
//                    body = JsonUtil.parseMiddleData(code, "text");
                }catch (Exception e){
                    return error("请重新请求");
                }
            }else {
                return error("我没有搜索出来答案");
            }
            if (StrUtil.equals(is_employ_ask,"true")){
                TbAnsweEmploy tbAnsweEmploy = new TbAnsweEmploy();
                tbAnsweEmploy.setAnserTitle(aivo.getPrompt());
                tbAnsweEmploy.setAnserContent(body);
                int i = iTbAnsweEmployService.insertTbAnsweEmployAuto(tbAnsweEmploy);
                if (i==1){
                    if (!StrUtil.equals(back_ask,"0")){
                        return success(body+"\n"+back_ask);
                    }
                    return success(body);
                }else {
                    return error("请求错误");
                }
            }else {
                if (!StrUtil.equals(back_ask,"0")){
                    return success(body+"\n"+back_ask);
                }
                return success(body);
            }
        }
        return error("请求错误");
    }
    /**
     * 获取配置信息
     * @return
     */
    @PostMapping("/configInfo")
    public AjaxResult chat() {
        //小程序名称
        String weichat_name= iSysConfigService.selectConfigByKey("weichat_name");
        //小程序广告
        String weichat_adv = iSysConfigService.selectConfigByKey("weichat_adv");
        //小程序公告
        String weichat_notice = iSysConfigService.selectConfigByKey("weichat_notice");
        Map<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("weichat_name",weichat_name);
        objectObjectHashMap.put("weichat_adv",weichat_adv);
        objectObjectHashMap.put("weichat_notice",weichat_notice);
        return success(objectObjectHashMap);
    }


    /**
     * 插入openid以及另一个信息,初始化个人信息
     * @return
     */
    @PostMapping("/insertOpinID")
    public AjaxResult InsertOpinID(@RequestBody TbAnsweUser tbAnsweUser) {
        if (StrUtil.isBlank(tbAnsweUser.getJs_code())){
            return error("请求参数为空");
        }
        JSONObject jsonObject = weiXinUtil.getSessionkey(tbAnsweUser.getJs_code());
        String session_key = jsonObject.getStr("session_key");
        String openid = jsonObject.getStr("openid");
        //根据openID去查询,看是否存在该用户
        TbAnsweUser tbAnsweUserSelect = iTbAnsweUserService.selectTbAnsweUserByOpenId(openid);
        if (Objects.isNull(tbAnsweUserSelect)){
            tbAnsweUserSelect = tbAnsweUser;
            if (StrUtil.isBlank(tbAnsweUserSelect.getAnsweUserName())){
                return error("用户名为空");
            }
            if (StrUtil.isBlank(tbAnsweUserSelect.getAnsweUserAvatar())){
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
     * @return
     */
    @GetMapping("/checkIsIng")
    public Boolean checkIsIng(String answeUserOpenid){
        Object o = redisTemplate.opsForValue().get(answeUserOpenid);
        if (Objects.isNull(o)){
            return  false;
        }
        return true;
    }


    @PostMapping("/chatBot")
    public AjaxResult chatBot(@RequestBody TbAnsweUser tbAnsweUser) {

        if (StrUtil.isBlank(tbAnsweUser.getAnsweUserOpenid())){
            return   error("请您先登陆");
        }
        Object o = redisTemplate.opsForValue().get(tbAnsweUser.getAnsweUserOpenid());
        if (!Objects.isNull(o)){
            return   error("正在回复消息");
        }
        redisTemplate.opsForValue().set(tbAnsweUser.getAnsweUserOpenid(),true,30, TimeUnit.SECONDS);
        TbAnsweUser tbAnsweUserSelect = iTbAnsweUserService.selectTbAnsweUserByOpenId(tbAnsweUser.getAnsweUserOpenid());
        if (Objects.isNull(tbAnsweUserSelect)){
            redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
            return   error("您暂无授权");
        }
        //是否检查
        String is_check_ask = iSysConfigService.selectConfigByKey("is_check_ask");
        if (StrUtil.isBlank(tbAnsweUser.getPrompt())){
            redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
            return   error("输入内容为空");
        }

        if (StrUtil.equals(is_check_ask,"1")){
            // 通知内容添加文本铭感词汇过滤
            //其余错误见返回码说明
            //正常返回0
            Integer isSensitive = null;
            try {
                isSensitive = weiXinUtil.msgCheck(tbAnsweUser.getPrompt());
            }catch (Exception e){
                redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
                return error("请重新发送");
            }
            //当图片文件内含有敏感内容，则返回87014
            if (Objects.isNull(isSensitive)){
                redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
                return error("请重新访问");
            }
            if (isSensitive==87014) {
                redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
                return error("含有违禁词");
            }else if (isSensitive!=0){
                redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
                return error("查询出错,请联系管理");
            }
        }
        String body = "";
        //获取数据方式:1表示获取官方数据,2获取接口转发数据
        String get_data_type = iSysConfigService.selectConfigByKey("get_data_type");
        try {
        if (StrUtil.equals(get_data_type,"2")){
            //暂无逻辑
        }else {
            AjaxResult ajaxResult = officalGetData(tbAnsweUser);
            Integer codeR =(Integer)ajaxResult.get("code");
            if (codeR!=200){
                redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
                return ajaxResult;
            }
//            ajaxResult.get
            String apikey =(String)ajaxResult.get("data");
            //请求URL
            String  url = "https://api.openai.com/v1/completions";
            String [] a = new String[]{"\n"};
            Map<Object, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("model","text-davinci-003");
            objectObjectHashMap.put("prompt",tbAnsweUser.getPrompt());
            objectObjectHashMap.put("max_tokens",2048);
            String postData = JSONUtil.toJsonStr(objectObjectHashMap);

            body = HttpRequest.post(url)
                    .header("Authorization", "Bearer "+apikey)//头信息，多个头信息多次调用此方法即可
                    .header("Content-Type","application/json")
                    .body(postData)//表单内容
                    .timeout(200000)//超时，毫秒
                    .execute().body();
        }
        }catch (Exception e){
            redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
            return error("请求错误");
        }

        if (StrUtil.isNotBlank(body)){
            if (StrUtil.contains(body,"statusCode") && StrUtil.contains(body,"TooManyRequests")){
                redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
                return error("请求错误");
            }
            if (StrUtil.contains(body,"code") && Objects.isNull(JsonUtil.parseMiddleData(body, "code"))){
                //刷新
                redisTemplate.delete("apikey");
                officalGetData(tbAnsweUser);
                redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
                return error("请重新发送");
            }else if (StrUtil.equals(JsonUtil.parseMiddleData(body, "model"),"text-davinci-003")){
                try {
                    String code = JsonUtil.parseMiddleData(body, "choices");
                    JSONArray jsonArray = JSONUtil.parseArray(code);
                    body = jsonArray.getJSONObject(0).getStr("text");
//                    body = JsonUtil.parseMiddleData(code, "text");
                }catch (Exception e){
                    redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
                    return error("请重新请求");
                }
            }else {
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
            if (!StrUtil.equals(back_ask,"0")){
                //添加回复内容
                contentVo.setText(body+"\n"+back_ask);
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


    @PostMapping("/chatSave")
    public AjaxResult chatSave(@RequestBody TbAnsweUser tbAnsweUser) {

        if (StrUtil.isBlank(tbAnsweUser.getAnsweUserOpenid())){
            return   error("请您先登陆");
        }
        if (StrUtil.isBlank(tbAnsweUser.getAnsweUserJson())){
            return   error("内容为空");
        }
        TbAnsweUser tbAnsweUserSelect = iTbAnsweUserService.selectTbAnsweUserByOpenId(tbAnsweUser.getAnsweUserOpenid());
        if (Objects.isNull(tbAnsweUserSelect)){
            return   error("您暂无授权");
        }
        tbAnsweUserSelect.setAnsweUserJson(tbAnsweUser.getAnsweUserJson());
        int i = iTbAnsweUserService.updateTbAnsweUser(tbAnsweUserSelect);
        if (i==1){
            return success("请求成功");
        }
        return error("请求错误");
    }


    /**
     * 官方接口获取
     */
    public AjaxResult officalGetData( TbAnsweUser tbAnsweUser){

        //获取是否收录问题
        String get_key_url = iSysConfigService.selectConfigByKey("get_key_url");
        Object apikey = redisTemplate.opsForValue().get("apikey");
        String get_key_self = iSysConfigService.selectConfigByKey("get_key_self");
        if (!StrUtil.equals(get_key_self,"0")){
            apikey =  get_key_self;
        }
        if (Objects.isNull(apikey)){
            String getKey = HttpRequest.get(get_key_url)
                    .timeout(200000)//超时，毫秒
                    .execute().body();
            if (StrUtil.isNotBlank(getKey)){
                JSONObject jsonObject = JSONUtil.parseObj(getKey);
                Integer statusCode = jsonObject.getInt("statusCode");
                if (statusCode==200){
                    apikey =   jsonObject.getStr("data");
                    redisTemplate.opsForValue().set("apikey",apikey);
                    //一直更换到合适的key
                    changeKey(tbAnsweUser);
                    return success(apikey);
                }else {
                    redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
                    return error("系统故障,多次尝试无果请直接小程序反馈");
                }
            }else {
                redisTemplate.delete(tbAnsweUser.getAnsweUserOpenid());
                return error("系统故障,多次尝试无果请直接小程序反馈");
            }
        }
        return success(apikey);
    }
    /**
     * 官方接口获取
     */
    public AjaxResult officalGetDataAsk(){

        //获取是否收录问题
        String get_key_url = iSysConfigService.selectConfigByKey("get_key_url");
        Object apikey = redisTemplate.opsForValue().get("apikey");

        String get_key_self = iSysConfigService.selectConfigByKey("get_key_self");
        if (!StrUtil.equals(get_key_self,"0")){
            apikey =  get_key_self;
        }
        //apikey = "填写你自己的key即可,然后放开即可";
        if (Objects.isNull(apikey)){
            String getKey = HttpRequest.get(get_key_url)
                    .timeout(200000)//超时，毫秒
                    .execute().body();
            if (StrUtil.isNotBlank(getKey)){
                JSONObject jsonObject = JSONUtil.parseObj(getKey);
                Integer statusCode = jsonObject.getInt("statusCode");
                if (statusCode==200){
                    apikey =   jsonObject.getStr("data");
                    redisTemplate.opsForValue().set("apikey",apikey);
                    //一直更换到合适的key
                    changeKeyAsk();
                    return success(apikey);
                }else {
                    return error("系统故障,多次尝试无果请直接小程序反馈");
                }
            }else {
                return error("系统故障,多次尝试无果请直接小程序反馈");
            }
        }
        return success(apikey);
    }
   public void changeKey(TbAnsweUser tbAnsweUser){
       Object apikey = redisTemplate.opsForValue().get("apikey");
       String input = "你好";
       //请求URL
       String  url = "https://api.openai.com/v1/completions";

       String [] a = new String[]{"\n"};
       Map<Object, Object> objectObjectHashMap = new HashMap<>();
       objectObjectHashMap.put("model","text-davinci-003");
       objectObjectHashMap.put("prompt",input);
       objectObjectHashMap.put("max_tokens",2048);
       String postData = JSONUtil.toJsonStr(objectObjectHashMap);
       String result2 = HttpRequest.post(url)
               .header("Authorization", "Bearer "+apikey)//头信息，多个头信息多次调用此方法即可
               .header("Content-Type","application/json")
               .body(postData)//表单内容
               .timeout(200000)//超时，毫秒
               .execute().body();
       if (StrUtil.isNotBlank(result2)){
           String error = JsonUtil.parseMiddleData(result2, "error");
           String type = JsonUtil.parseMiddleData(error, "type");
           if (StrUtil.equals(type,"insufficient_quota")){
               officalGetData(tbAnsweUser);
           }
       }
    }
    public void changeKeyAsk(){
        Object apikey = redisTemplate.opsForValue().get("apikey");
        String input = "你好";
        //请求URL
        String  url = "https://api.openai.com/v1/completions";

        String [] a = new String[]{"\n"};
        Map<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("model","text-davinci-003");
        objectObjectHashMap.put("prompt",input);
        objectObjectHashMap.put("max_tokens",2048);
        String postData = JSONUtil.toJsonStr(objectObjectHashMap);
        String result2 = HttpRequest.post(url)
                .header("Authorization", "Bearer "+apikey)//头信息，多个头信息多次调用此方法即可
                .header("Content-Type","application/json")
                .body(postData)//表单内容
                .timeout(200000)//超时，毫秒
                .execute().body();
        if (StrUtil.isNotBlank(result2)){
            String error = JsonUtil.parseMiddleData(result2, "error");
            String type = JsonUtil.parseMiddleData(error, "type");
            if (StrUtil.equals(type,"insufficient_quota")){
                officalGetDataAsk();
            }
        }
    }

}
