package com.ruoyi.ai.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.ruoyi.util.weixin.WxAppUtilService;
import com.ruoyi.util.weixin.WxCommonUtilService;
import com.ruoyi.ai.doamin.RegisterOrLoginVO;
import com.ruoyi.ai.service.IWechatLoginService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.util.weixin.WxH5UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@RequestMapping("/authorization/wx")
@RestController
public class WeChatLoginController {
    @Autowired
    private IWechatLoginService iWechatLoginService;
    @Autowired
    private WxH5UtilService wxH5InfoMation;
    @Autowired
    private WxAppUtilService wxAppUtilService;
    @Autowired
    private WxCommonUtilService wxCommonUtilService;
    @Autowired
    private ISysUserService iSysUserService;

    /**
     * 用户ID - 微信小程序登录以及
     * @param registerOrLoginVO
     * @param httpSession
     * @return
     */
    @Anonymous
    @PostMapping("/registerOrLogin")
    public AjaxResult registerOrLogin(@RequestBody RegisterOrLoginVO registerOrLoginVO, HttpSession httpSession)
    {
        if (StrUtil.isBlank(registerOrLoginVO.getJsCode())) {
            throw new RuntimeException("请求参数为空");
        }
        JSONObject jsonObject = wxAppUtilService.getSessionkey(registerOrLoginVO.getJsCode());
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


    /**
     * 用户ID - 微信小程序登录以及
     * @param registerOrLoginVO
     * @param httpSession
     * @return
     */
    @Anonymous
    @PostMapping("/h5/registerOrLogin")
    public AjaxResult h5RegisterOrLogin(@RequestBody RegisterOrLoginVO registerOrLoginVO, HttpSession httpSession)
    {
        if (StrUtil.isBlank(registerOrLoginVO.getJsCode())) {
            throw new RuntimeException("请求参数为空");
        }
        JSONObject jsonObject = wxH5InfoMation.wxH5InfoMation(registerOrLoginVO.getJsCode());
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
