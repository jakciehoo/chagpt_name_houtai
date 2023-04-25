package com.ruoyi.ai.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.ruoyi.ai.service.IExtendSysUserService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@RequestMapping("/authorization")
@RestController
public class CommonLoginController {
    @Autowired
    private IExtendSysUserService iExtendSysUserService;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private IWechatLoginService iWechatLoginService;
    /**
     * 用户ID - 发送短信Code
     * @param registerOrLoginVO
     * @return
     */
    @Anonymous
    @PostMapping("/register/sendCmsCode")
    public AjaxResult sendCmsCode(@RequestBody RegisterOrLoginVO registerOrLoginVO,HttpServletRequest httpServletRequest)
    {
         iExtendSysUserService.sendCmsCode(httpServletRequest,registerOrLoginVO.getUserName(),"login");
         return AjaxResult.success("发送成功");
    }

    /**
     * 用户ID - 校验短信Code
     * @param registerOrLoginVO
     * @return
     */
    @Anonymous
    @PostMapping("/register/smsLogin")
    public AjaxResult smsLogin(@RequestBody RegisterOrLoginVO registerOrLoginVO, HttpSession httpSession)
    {
        if (StrUtil.isBlank(registerOrLoginVO.getUserName())){
            throw new RuntimeException("手机号不可为空");
        }
        if (StrUtil.isBlank(registerOrLoginVO.getValismsCode())){
            throw new RuntimeException("验证码不可为空");
        }
        iExtendSysUserService.checkSmsCode(registerOrLoginVO.getUserName(),registerOrLoginVO.getValismsCode(),"login");
        //验证码校验完毕后

        //根据openID去查询,看是否存在该用户
        SysUser sysUser = iSysUserService.selectUserByUserName(registerOrLoginVO.getUserName());
        if (!Objects.isNull(sysUser)){
            //进行登陆操作
            return iWechatLoginService.wxLogin(sysUser,httpSession);
        }else {
            //进行登陆操作
            return iExtendSysUserService.webRegister(registerOrLoginVO,httpSession);
        }
    }
}
