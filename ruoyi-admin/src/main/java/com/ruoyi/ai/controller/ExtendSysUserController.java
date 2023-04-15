package com.ruoyi.ai.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.ai.service.IExtendSysUserService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class ExtendSysUserController {

    @Autowired
    private IExtendSysUserService iExtendSysUserService;


    @Autowired
    private ISysUserService iSysUserService;

    @GetMapping("/getUserInfo")
    public AjaxResult registerOrLogin() {
        return AjaxResult.success(iExtendSysUserService.getUserInfo());
    }


    /**
     * 更新数据
     * @return
     */
    @PostMapping("/updateUserInfo")
    public AjaxResult updateUserInfo(@RequestBody SysUser sysUser) {
        return AjaxResult.success(iExtendSysUserService.updateUserInfo(sysUser));
    }

    /**
     * 更新数据
     * @return
     */
    @PostMapping("/updateUserVipType")
    public AjaxResult updateUserVipType(@RequestBody SysUser sysUser) {
        return AjaxResult.success(iExtendSysUserService.updateUserVipType(sysUser));
    }
}
