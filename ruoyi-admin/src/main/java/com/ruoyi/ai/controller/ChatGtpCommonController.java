package com.ruoyi.ai.controller;

import com.ruoyi.ai.doamin.SettingVO;
import com.ruoyi.ai.doamin.UpdateConfigVO;
import com.ruoyi.ai.service.IChatGtpService;
import com.ruoyi.chatgpt.service.ITbAnsweEmployService;
import com.ruoyi.chatgpt.service.ITbAnsweUserService;
import com.ruoyi.chatgpt.service.ITbKeyManagerService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.util.weixin.WxCommonUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ruoyi.common.core.domain.AjaxResult.error;
import static com.ruoyi.common.core.domain.AjaxResult.success;




/**
 * GPT流推送 - websocket
 */
@RequestMapping("/yuan/ai/common")
@RestController
public class ChatGtpCommonController {
    @Autowired
    private ITbAnsweEmployService iTbAnsweEmployService;
    @Autowired
    private ISysConfigService iSysConfigService;
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

    /**
     * 获取配置信息
     *
     * @return
     */
    @PostMapping("/configInfo")
    public AjaxResult configInfo() {
        return success(iChatGtpService.getConfigInfo());
    }


    /**
     * 获取配置信息
     *
     * @return
     */
    @GetMapping("/getConfigSys")
    @PreAuthorize("@ss.hasPermi('chatgpt:user:getConfigSys')")
    public AjaxResult getConfigSys() {
        return success(iChatGtpService.getConfigSys());
    }

    /**
     * 保存配置信息
     *
     * @return
     */
    @Anonymous
    @PostMapping("/saveConfigInfo")
    @PreAuthorize("@ss.hasPermi('chatgpt:user:saveConfigInfo')")
    public AjaxResult saveConfigInfo(@RequestBody List<SettingVO> formData) {
        return success(iChatGtpService.saveConfigInfo(formData));
    }






    /**
     * 同步配置
     *
     * @return
     */
    @Anonymous
    @PostMapping("/sysnConfigInfo")
    @PreAuthorize("@ss.hasPermi('chatgpt:user:sysnConfigInfo')")
    public AjaxResult sysnConfigInfo(@RequestBody UpdateConfigVO updateConfigVO) {
        return success(iChatGtpService.sysnConfigInfo(updateConfigVO));

    }

}
