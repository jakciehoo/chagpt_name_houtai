package com.ruoyi.ai.service;

import com.ruoyi.ai.doamin.RegisterOrLoginVO;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;

import javax.servlet.http.HttpSession;

public interface IWechatLoginService {
    AjaxResult wxregister(RegisterOrLoginVO registerOrLoginVO,HttpSession httpSession);
    AjaxResult wxLogin(SysUser sysUser, HttpSession httpSession);

    void intiveAddNum(RegisterOrLoginVO registerOrLoginVO,SysUser sysUserRegister);
}
