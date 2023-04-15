package com.ruoyi.ai.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.ruoyi.ai.WeiXinUtilService;
import com.ruoyi.ai.doamin.RegisterOrLoginVO;
import com.ruoyi.ai.service.IWechatLoginService;
import com.ruoyi.chatgpt.domain.TbAnsweUser;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@RequestMapping("/authorization")
@RestController
public class WechatLoginController {
    @Autowired
    private IWechatLoginService iWechatLoginService;
    @Autowired
    private WeiXinUtilService weiXinUtilService;
    @Autowired
    private ISysUserService iSysUserService;

    /**
     * 用户ID
     * @param registerOrLoginVO
     * @param httpSession
     * @return
     */
    @Anonymous
    @PostMapping("/wx/registerOrLogin")
    public AjaxResult registerOrLogin(@RequestBody RegisterOrLoginVO registerOrLoginVO, HttpSession httpSession)
    {
        if (StrUtil.isBlank(registerOrLoginVO.getJsCode())) {
            throw new RuntimeException("请求参数为空");
        }
        JSONObject jsonObject = weiXinUtilService.getSessionkey(registerOrLoginVO.getJsCode());
        String openid = jsonObject.getStr("openid");
        if (StrUtil.isBlank(openid)){
            throw new RuntimeException("请确认小程序ID以及密钥填写正确");
        }
        registerOrLoginVO.setOpenid(openid);
        //根据openID去查询,看是否存在该用户
        SysUser sysUser = iSysUserService.checkOpenIDUnique(openid);
        if (!Objects.isNull(sysUser)){
            //进行登陆操作
           return iWechatLoginService.wxLogin(sysUser,httpSession);
        }else {
           return iWechatLoginService.wxregister(registerOrLoginVO,httpSession);
        }

    }
}
