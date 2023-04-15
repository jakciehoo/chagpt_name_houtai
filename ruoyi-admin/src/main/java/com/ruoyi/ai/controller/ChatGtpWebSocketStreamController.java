package com.ruoyi.ai.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.ruoyi.ai.*;
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
import com.ruoyi.webSocket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.ruoyi.common.core.domain.AjaxResult.error;
import static com.ruoyi.common.core.domain.AjaxResult.success;




/**
 * GPT流推送 - websocket
 */
@RequestMapping("/yuan/ai/w/stream")
@RestController
public class ChatGtpWebSocketStreamController {
    @Autowired
    private ITbAnsweEmployService iTbAnsweEmployService;
    @Autowired
    private IconfigService iconfigService;
    @Autowired
    private IChatGtpService iChatGtpService;
    @Autowired
    private WeiXinUtilService weiXinUtil;
    @Autowired(required = false)
    private RedisTemplate<Object, Object> redisTemplate;


    @PostMapping("/wordSay")
    @PreAuthorize("@ss.hasPermi('system:rechargeablecardmain:remove')")
    public AjaxResult wordSay(String content) {
        String all = "{\"id\":\"chatcmpl-75DdeXJ3CqJpjlTN5Ze9pSP1HBGtZ\",\"object\":\"chat.completion.chunk\",\"created\":1681478422,\"model\":\"gpt-3.5-turbo-0301\",\"choices\":[{\"delta\":{\"content\":\""+content+"\"},\"index\":0,\"finish_reason\":null}]}";
        WebSocketService.sendWord(all);
       return success("喊话成功");
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





    /**
     * 检查是否回复中
     *
     * @return
     */
    @GetMapping("/checkIsIng")
    public AjaxResult checkIsIng(@RequestParam String userId) {
        Object o = redisTemplate.opsForValue().get(userId);
        return success(!Objects.isNull(o));
    }

    /**
     * 对打模式-不支持连续问话
     * @param streamParametersVO
     * @return
     */
    @PostMapping("/chat")
    public AjaxResult chat(@RequestBody StreamParametersVO streamParametersVO) {
        iChatGtpService.chatSocketStream(streamParametersVO);
        return success("success");
    }

    /**
     * 续问模式 - 支持连续问话
     * @param streamParametersVO
     * @return
     */
    @PostMapping("/chatBot")
    public AjaxResult chatBot(@RequestBody StreamParametersVO streamParametersVO) {
        iChatGtpService.chatBotSocketStream(streamParametersVO);
        return success("success");
    }

    /**
     * 统计
     * @return
     */
    @PostMapping("/statisticsData")
    public AjaxResult statisticsData() {
         return  success(iChatGtpService.statisticsData());
    }





































}
