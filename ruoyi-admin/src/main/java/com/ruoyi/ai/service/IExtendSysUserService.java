package com.ruoyi.ai.service;

import com.ruoyi.ai.doamin.RegisterOrLoginVO;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

public interface IExtendSysUserService {
    /**
     * 查询余额次数
     */
    public Integer blanceNumQuery();

    /**
     * 增加余额次数
     */
    public Integer blanceAddNumChange(SysUser sysUser,Integer addBlance);
    public Integer  blanceAddNumRegisterChange(SysUser sysUser,Integer addBlance);
    /**
     * 减少余额次数
     */
    public Integer blanceDecreaseNumChange(SysUser sysUser, Integer dcreaseBlance);

    /**
     * 查询可用时间
     */
    public Date blanceDateQuery();


    /**
     * 增加可用时间
     */
    public Integer blanceAddDateChange(SysUser user,Integer addBlance);


    public  void numOrDateAddOrDe();

    public  SysUser getUserInfo();
    public  Integer updateUserInfo(SysUser sysUser);
    public  Integer updateUserVipType(SysUser sysUser);

    /**
     * 发送验证码
     * @param httpServletRequest
     * @param userName
     * @param status
     */
    public  void sendCmsCode(HttpServletRequest httpServletRequest, String userName, String status);

    /**
     *校验验证码
     * @param userName
     * @param valismsCode
     * @param status
     */
    public  void checkSmsCode(String userName,String valismsCode, String status);


    AjaxResult webRegister(RegisterOrLoginVO registerOrLoginVO, HttpSession httpSession);
}
