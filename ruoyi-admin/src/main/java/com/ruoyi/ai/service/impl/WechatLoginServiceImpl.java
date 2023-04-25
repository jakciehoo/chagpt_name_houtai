package com.ruoyi.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.ai.doamin.RegisterOrLoginVO;
import com.ruoyi.ai.doamin.WxUserReturnVO;
import com.ruoyi.ai.service.IChatGtpService;
import com.ruoyi.ai.service.IExtendSysUserService;
import com.ruoyi.ai.service.IWechatLoginService;
import com.ruoyi.ai.service.IconfigService;
import com.ruoyi.chatgpt.domain.TbFrequencyConsumptionLog;
import com.ruoyi.chatgpt.service.ITbFrequencyConsumptionLogService;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.SysRegisterService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class WechatLoginServiceImpl implements IWechatLoginService {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private SysLoginService loginService;
    @Autowired
    private IconfigService iconfigService;
    @Autowired
    private IChatGtpService iChatGtpService;
    @Autowired
    private IExtendSysUserService iExtendSysUserService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ITbFrequencyConsumptionLogService iTbFrequencyConsumptionLogService;
    @Autowired
    private  ISysRoleService iSysRoleService;
    @Autowired
    private SysRegisterService sysRegisterService;
    /**
     * 注册
     */
    @Override
    @Transactional
    public AjaxResult wxregister(RegisterOrLoginVO registerOrLoginVO, HttpSession httpSession)
    {
        String openId = registerOrLoginVO.getOpenid();
        //获取默认的用户头像
        String defaultPhotoImage = iconfigService.selectConfigByKey("default_photo_image");
        //默认注册次数
        String registerGiveNumber = iconfigService.selectConfigByKey("register_give_number");
        if (StrUtil.isBlank(registerGiveNumber)){
            registerGiveNumber = "0";
        }
        //如果不存在用户就进行注册
        //注册获取普通用户的权限
        SysUser sysUserRegister = new SysUser();
        sysUserRegister.setUserName(openId);
        String username_before = iconfigService.selectConfigByKey("username_before");
        if (StrUtil.isBlank(username_before)){
            username_before= "元AI";
        }
        String  nickName = username_before+"_"+ StrUtil.sub(openId,3,9)+ RandomUtil.randomNumbers(1);
        sysUserRegister.setNickName(nickName);
        sysUserRegister.setAvatar(defaultPhotoImage);
        sysUserRegister.setOpenId(openId);
        sysUserRegister.setSex("0");
        sysUserRegister.setDeptId(110l);
        sysUserRegister.setInvitationCode(StrUtil.sub(openId,2,12)+ RandomUtil.randomNumbers(1));
        Long[] roleIds = new Long[] {2l};
        //设置默认用户组
        String default_common_usergroup = iconfigService.selectConfigByKey("default_common_usergroup");
        if (StrUtil.isBlank(default_common_usergroup)){
            default_common_usergroup ="2";
        }
        SysRole sysRole = iSysRoleService.selectRoleById(Long.valueOf(default_common_usergroup));
        List<SysRole> sysRoles = new ArrayList<>();
        sysRoles.add(sysRole);
        sysUserRegister.setRoles(sysRoles);
        sysUserRegister.setBlanceNum(Integer.valueOf(registerGiveNumber));
        sysUserRegister.setBlanceDate(DateTime.now());
        sysUserRegister.setVipType(1);
        sysUserRegister.setRoleIds(roleIds);
        String wxregisterTokenOrMssge = sysRegisterService.wxregister(sysUserRegister);
        AjaxResult ajax = AjaxResult.success();
        Map<String, Object> returnMap = new HashMap<>();
        WxUserReturnVO wxUserReturnVO = new WxUserReturnVO();
        BeanUtil.copyProperties(sysUserRegister, wxUserReturnVO);
        returnMap.put(Constants.TOKEN, wxregisterTokenOrMssge);
        returnMap.put("userInfo",wxUserReturnVO);
        ajax.put("data",returnMap);
        httpSession.setAttribute("user",sysUserRegister);
        //是否开启邀请送次数
        String is_open_intive_register_give_num = iconfigService.selectConfigByKey("is_open_intive_register_give_num");
        String is_open_num = iconfigService.selectConfigByKey("is_open_num");
        if (StrUtil.equals(is_open_intive_register_give_num,"1") && StrUtil.equals(is_open_num,"1")){
            this.intiveAddNum(registerOrLoginVO,sysUserRegister);
        }
        return ajax;
    }

    /**
     * 邀请成功后进行次数给予
     */
    @Transactional
    public void intiveAddNum(RegisterOrLoginVO registerOrLoginVO,SysUser sysUserRegister){
        if (StrUtil.isNotBlank(registerOrLoginVO.getInviteCode())){
            //邀请人信息
            SysUser sysUser = userService.selectUserByintiveCode(registerOrLoginVO.getInviteCode());
            if (Objects.isNull(sysUser)){
                throw new RuntimeException("邀请码不存在");
            }
            //获取邀请赠送数量
            String intive_give_num = iconfigService.selectConfigByKey("intive_give_num");
            if (StrUtil.isNotBlank(intive_give_num) && NumberUtil.isNumber(intive_give_num)){
                //增加次数
                iExtendSysUserService.blanceAddNumRegisterChange(sysUser, Integer.valueOf(intive_give_num));
                //增加日志
                TbFrequencyConsumptionLog tbFrequencyConsumptionLog = new TbFrequencyConsumptionLog();
                tbFrequencyConsumptionLog.setRemark(intive_give_num);
                tbFrequencyConsumptionLog.setChangeContent("邀请【" + sysUserRegister.getUserId() + "-"+sysUserRegister.getNickName()+"】用户,增加" + intive_give_num+ "次问答次数。");
                tbFrequencyConsumptionLog.setOperationType(4l);
                tbFrequencyConsumptionLog.setOperationName("新用户邀请");
                tbFrequencyConsumptionLog.setOperationId(sysUserRegister.getUserId() + "");
                tbFrequencyConsumptionLog.setUserId(sysUser.getUserId());
                int insertStatus = iTbFrequencyConsumptionLogService.insertTbFrequencyConsumptionLogRegister(tbFrequencyConsumptionLog);
                if (insertStatus != 1) {
                    throw new RuntimeException("注册失败");
                }
            }
        }
    }




    /**
     * 微信登录
     * @param sysUser
     * @return
     */
    @Override
    public AjaxResult wxLogin(SysUser sysUser, HttpSession httpSession) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.wxlogin(sysUser);
        Map<String, Object> returnMap = new HashMap<>();
        WxUserReturnVO wxUserReturnVO = new WxUserReturnVO();
        BeanUtil.copyProperties(sysUser, wxUserReturnVO);
        returnMap.put(Constants.TOKEN, token);
        returnMap.put("userInfo",wxUserReturnVO);
        ajax.put("data",returnMap);
        httpSession.setAttribute("user",sysUser);

        return ajax;
    }

}
