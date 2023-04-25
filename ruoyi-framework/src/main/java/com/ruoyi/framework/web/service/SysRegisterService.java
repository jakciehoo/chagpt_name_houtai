package com.ruoyi.framework.web.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.*;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.web.domain.SettingSVO;
import com.ruoyi.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 注册校验方法
 *
 * @author ruoyi
 */
@Component
public class SysRegisterService
{
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    @Lazy
    private CaptchaService captchaService;

    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private ISysConfigService iSysConfigService;
    /**
     * 注册
     */
    public String register(RegisterBody registerBody)
    {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);

        // 验证码开关
//        boolean captchaEnabled = configService.selectCaptchaEnabled();
//        if (captchaEnabled)
//        {
//            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
//        }

        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(registerBody.getCode());
        ResponseModel response = captchaService.verification(captchaVO);
        if (!response.isSuccess())
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }

        if (StringUtils.isEmpty(username))
        {
            msg = "用户名不能为空";
        }
        else if (StringUtils.isEmpty(password))
        {
            msg = "用户密码不能为空";
        }
        else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            msg = "账户长度必须在11到20个字符之间";
        }
        else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            msg = "密码长度必须在5到20个字符之间";
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(sysUser)))
        {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        }
        else
        {
            //获取默认的用户头像
            String defaultPhotoImage = selectConfigByKey("default_photo_image");
            //默认注册次数
            String registerGiveNumber = selectConfigByKey("register_give_number");
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            String username_before = selectConfigByKey("username_before");
            if (StrUtil.isBlank(username_before)){
                username_before= "元AI";
            }
            sysUser.setNickName(username_before+"_"+StrUtil.sub(username,1,5));
            sysUser.setAvatar(defaultPhotoImage);
            sysUser.setOpenId(null);
            sysUser.setSex("0");
            sysUser.setDeptId(110l);
            sysUser.setInvitationCode(StrUtil.sub(username,1,8)+ RandomUtil.randomNumbers(3));
            Long[] roleIds = new Long[] {2l};
            //设置默认用户组
            String default_common_usergroup = selectConfigByKey("default_common_usergroup");
            if (StrUtil.isBlank(default_common_usergroup)){
                default_common_usergroup ="2";
            }
            SysRole sysRole = iSysRoleService.selectRoleById(Long.valueOf(default_common_usergroup));
            List<SysRole> sysRoles = new ArrayList<>();
            sysRoles.add(sysRole);
            sysUser.setRoles(sysRoles);
            sysUser.setBlanceNum(Integer.valueOf(registerGiveNumber));
            sysUser.setBlanceDate(DateTime.now());
            sysUser.setVipType(1);
            sysUser.setRoleIds(roleIds);
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag)
            {
                msg = "注册失败,请联系系统管理人员";
            }
            else
            {
                iSysRoleService.insertDefaultRole(sysUser.getUserId());
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        return msg;
    }

    public String selectConfigByKey(String key)
    {
        String baseConfigJson = iSysConfigService.selectConfigByKey("baseConfigJson");
        List<SettingSVO> settingVOList = JSON.parseArray(baseConfigJson, SettingSVO.class);
        Map<String, String> getValue = settingVOList.stream().collect(Collectors.toMap(SettingSVO::getKey, SettingSVO::getValue));
        return getValue.get(key);
    }
    /**
     * 注册
     */
    @Transactional
    public String wxregister(SysUser sysUser)
    {
        String msg = "";
        String  username = sysUser.getUserName();
        if (StringUtils.isEmpty(username))
        {
            throw new RuntimeException("用户名不能为空");
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(sysUser)))
        {
            throw new RuntimeException("请重新注册");
        }
        else
        {
//            sysUser.setNickName(username);
//            userService.insertUser
            int saveStatus = userService.insertUser(sysUser);
            if (saveStatus!=1)
            {
                msg = "注册失败,请联系系统管理人员";
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success")));
                recordLoginInfo(sysUser.getUserId());
                Set<String> menuPermission = sysPermissionService.getMenuPermission(sysUser);
                System.out.println(menuPermission);
                LoginUser loginUser = new LoginUser();
                loginUser.setPermissions(menuPermission);
                loginUser.setDeptId(sysUser.getDeptId());
                loginUser.setUserId(sysUser.getUserId());
                loginUser.setUser(sysUser);
                // 生成token
                return tokenService.createToken(loginUser);
            }
        }
        return msg;
    }



    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }
    }

    /**
     * 记录登录信息
     *-
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }
}
