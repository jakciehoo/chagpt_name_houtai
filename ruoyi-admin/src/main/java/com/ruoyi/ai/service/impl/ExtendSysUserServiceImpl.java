package com.ruoyi.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.ai.doamin.RegisterOrLoginVO;
import com.ruoyi.ai.doamin.WxUserReturnVO;
import com.ruoyi.ai.service.IExtendSysUserService;
import com.ruoyi.ai.service.IWechatLoginService;
import com.ruoyi.ai.service.IconfigService;
import com.ruoyi.chatgpt.service.ITbFrequencyConsumptionLogService;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.SysRegisterService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.util.sms.SmsUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

import static com.ruoyi.common.core.domain.AjaxResult.error;

@Service
public class ExtendSysUserServiceImpl implements IExtendSysUserService {
    @Autowired
    private IconfigService iconfigService;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private SmsUtilService smsUtilService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private SysRegisterService sysRegisterService;

    @Autowired
    private IWechatLoginService iWechatLoginService;
    @Override
    public  Integer blanceNumQuery() {
        SysUser sysUser = iSysUserService.selectUserById(SecurityUtils.getUserId());
        //获取余额次数
        Integer blanceNum = sysUser.getBlanceNum();
        if (Objects.isNull(blanceNum)){
            blanceNum = 0;
        }
        return blanceNum;
    }

    @Override
    @Transactional
    public  Integer blanceAddNumChange(SysUser sysUser,Integer addBlance) {
        //获取余额次数
        Integer blanceNum = sysUser.getBlanceNum();
        sysUser.setBlanceNum(blanceNum+addBlance);
        int i = iSysUserService.updateUser(sysUser);
        if (i == 1) {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            loginUser.setUser(sysUser);
            tokenService.setLoginUser(loginUser);
            return i;
        }else {
            throw new RuntimeException("对话次数增加失败");
        }
    }

    @Override
    @Transactional
    public  Integer blanceDecreaseNumChange(SysUser sysUser,Integer dcreaseBlance) {
        //获取余额次数
        Integer blanceNum = sysUser.getBlanceNum();
        if (Objects.isNull(blanceNum)){
            blanceNum = 0;
        }
        //判断余额次数是否大于0
        if (blanceNum<=0){
            throw new RuntimeException("对话次数不足");
        }
        sysUser.setBlanceNum(blanceNum-dcreaseBlance);
        int i = iSysUserService.updateUserBlance(sysUser);
        if (i == 1) {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            loginUser.setUser(sysUser);
            tokenService.setLoginUser(loginUser);
            return i;
        }else {
            throw new RuntimeException("对话失败,次数未扣除");
        }
    }

    @Override
    @Transactional
    public  Date blanceDateQuery() {
        SysUser sysUser = iSysUserService.selectUserById(SecurityUtils.getUserId());
        //获取余额次数
        Date blanceDate = sysUser.getBlanceDate();
        if (Objects.isNull(blanceDate)){
            blanceDate = DateUtil.offset(DateTime.now(), DateField.SECOND, -1);
        }
        return blanceDate;
    }

    @Override
    @Transactional
    public  Integer blanceAddDateChange(SysUser user,Integer addBlance) {
        Date blanceDate = user.getBlanceDate();
        //判断时间是否过期
        if (DateTime.now().isAfter(blanceDate)){
            blanceDate = DateTime.now();
        }
        DateTime resultDate = DateUtil.offset(blanceDate, DateField.MINUTE, addBlance);
        user.setBlanceDate(resultDate);
        int i = iSysUserService.updateUser(user);
        if (i == 1) {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            loginUser.setUser(user);
            tokenService.setLoginUser(loginUser);
            return i;
        }else {
            throw new RuntimeException("对话时间增加失败");
        }
    }


    /**
     *
     * @return
     */
    @Override
    @Transactional
    public void numOrDateAddOrDe() {
        //获取用户信息
        SysUser user = iSysUserService.selectUserById(SecurityUtils.getUserId());
        //判断用户vip类型 -    /** 按次:1,按时间:2 */
        Integer vipType = user.getVipType();
        if (vipType==1){
           //按此进行计算
            this.catchByTimes(user);
        }else if (vipType==2){
           //按时间来算
            this.catchByDate();
        }
    }

    /**
     * 发送验证码
     * @param httpServletRequest
     * @param userName
     * @param status
     */
    public  void sendCmsCode(HttpServletRequest httpServletRequest, String userName, String status){
        try {
            smsUtilService.sendSmsCode(httpServletRequest,userName,status);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    public  void checkSmsCode(String userName,String valismsCode, String status){
        try {
            smsUtilService.checkSmsCode(userName,valismsCode,status);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public SysUser getUserInfo() {
        return iSysUserService.selectUserById(SecurityUtils.getUserId());
    }

    @Override
    public Integer updateUserInfo(SysUser sysUser) {
        if (StrUtil.isBlank(sysUser.getNickName())){
            throw new RuntimeException("昵称不可为空");
        }
        if (StrUtil.isBlank(sysUser.getAvatar())){
            throw new RuntimeException("头像不可为空");
        }
        SysUser sysUserInsert = iSysUserService.selectUserById(SecurityUtils.getUserId());
        sysUserInsert.setBlanceNum(null);
        sysUserInsert.setVipType(null);
        sysUserInsert.setBlanceDate(null);
        sysUserInsert.setNickName(sysUser.getNickName());
        sysUserInsert.setAvatar(sysUser.getAvatar());
        sysUserInsert.setUpdateTime(DateTime.now());
        return iSysUserService.updateUser(sysUserInsert);
    }
    @Override
    public Integer updateUserVipType(SysUser sysUser) {
        if (Objects.isNull(sysUser.getVipType())){
            throw new RuntimeException("切换失败");
        }
        SysUser sysUserInsert = iSysUserService.selectUserById(SecurityUtils.getUserId());
        sysUserInsert.setBlanceNum(null);
        sysUserInsert.setVipType(null);
        sysUserInsert.setBlanceDate(null);
        sysUserInsert.setVipType(sysUser.getVipType());
        sysUserInsert.setUpdateTime(DateTime.now());
        return iSysUserService.updateUser(sysUserInsert);
    }

    /**
     * 处理次数
     */
    @Transactional
    public void catchByTimes(SysUser sysUser){
        this.blanceDecreaseNumChange(sysUser,1);
    }
    /**
     * 处理按照时间
     */
    @Transactional
    public void catchByDate(){
        Date blanceDate = this.blanceDateQuery();
        if (blanceDate.before(DateTime.now())){
            throw new RuntimeException("时间过期");
        }
    }


    @Override
    @Transactional
    public  Integer blanceAddNumRegisterChange(SysUser sysUser,Integer addBlance) {
        //获取余额次数
        Integer blanceNum = sysUser.getBlanceNum();
        sysUser.setBlanceNum(blanceNum+addBlance);
        int i = iSysUserService.updateUser(sysUser);
        if (i == 1) {
            return i;
        }else {
            throw new RuntimeException("对话次数增加失败");
        }
    }


    @Override
    @Transactional
    public  AjaxResult webRegister(RegisterOrLoginVO registerOrLoginVO, HttpSession httpSession) {
        String username = registerOrLoginVO.getUserName();
        //获取默认的用户头像
        String defaultPhotoImage = iconfigService.selectConfigByKey("default_photo_image");
        //默认注册次数
        String registerGiveNumber = iconfigService.selectConfigByKey("register_give_number");
        if (StrUtil.isBlank(registerGiveNumber)){
            registerGiveNumber = "0";
        }
        //如果不存在用户就进行注册
        //注册获取普通用户的权限
        SysUser sysUser = new SysUser();
//        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        String username_before = iconfigService.selectConfigByKey("username_before");
        if (StrUtil.isBlank(username_before)){
            username_before= "元AI";
        }
        sysUser.setUserName(username);
        sysUser.setNickName(username_before+"_"+StrUtil.sub(username,1,5));
        sysUser.setAvatar(defaultPhotoImage);
        sysUser.setOpenId(null);
        sysUser.setSex("0");
        sysUser.setDeptId(110l);
        sysUser.setInvitationCode(StrUtil.sub(username,1,8)+ RandomUtil.randomNumbers(3));
        Long[] roleIds = new Long[] {2l};
        //设置默认用户组
        String default_common_usergroup = iconfigService.selectConfigByKey("default_common_usergroup");
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

        String wxregisterTokenOrMssge = sysRegisterService.wxregister(sysUser);
        AjaxResult ajax = AjaxResult.success();
        Map<String, Object> returnMap = new HashMap<>();
        WxUserReturnVO wxUserReturnVO = new WxUserReturnVO();
        BeanUtil.copyProperties(sysUser, wxUserReturnVO);
        returnMap.put(Constants.TOKEN, wxregisterTokenOrMssge);
        returnMap.put("userInfo",wxUserReturnVO);
        ajax.put("data",returnMap);
        httpSession.setAttribute("user",sysUser);
        //是否开启邀请送次数
        String is_open_intive_register_give_num = iconfigService.selectConfigByKey("is_open_intive_register_give_num");
        String is_open_num = iconfigService.selectConfigByKey("is_open_num");
        if (StrUtil.equals(is_open_intive_register_give_num,"1") && StrUtil.equals(is_open_num,"1")){
            iWechatLoginService.intiveAddNum(registerOrLoginVO,sysUser);
        }
        return ajax;
    }

}
