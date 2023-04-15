package com.ruoyi.chatgpt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.ai.service.IExtendSysUserService;
import com.ruoyi.chatgpt.domain.TbFrequencyConsumptionLog;
import com.ruoyi.chatgpt.domain.TbRechargeableCardMain;
import com.ruoyi.chatgpt.domain.TbRechargeableCardProcess;
import com.ruoyi.chatgpt.mapper.TbRechargeableCardProcessMapper;
import com.ruoyi.chatgpt.service.ITbFrequencyConsumptionLogService;
import com.ruoyi.chatgpt.service.ITbRechargeableCardMainService;
import com.ruoyi.chatgpt.service.ITbRechargeableCardProcessService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.time.TimeUtil;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 充值卡详情Service业务层处理
 *
 * @author zhx
 * @date 2023-03-27
 */
@Service
public class TbRechargeableCardProcessServiceImpl implements ITbRechargeableCardProcessService {
    @Autowired
    private TbRechargeableCardProcessMapper tbRechargeableCardProcessMapper;
    @Autowired
    private ITbRechargeableCardMainService iTbRechargeableCardMainService;
    @Autowired
    private IExtendSysUserService iExtendSysUserService;

    @Autowired
    private ITbFrequencyConsumptionLogService iTbFrequencyConsumptionLogService;

    @Autowired
    private ISysUserService iSysUserService;

    /**
     * 查询充值卡详情
     *
     * @param id 充值卡详情主键
     * @return 充值卡详情
     */
    @Override
    public TbRechargeableCardProcess selectTbRechargeableCardProcessById(Long id) {
        return tbRechargeableCardProcessMapper.selectTbRechargeableCardProcessById(id);
    }

    /**
     * 查询充值卡详情列表
     *
     * @param tbRechargeableCardProcess 充值卡详情
     * @return 充值卡详情
     */
    @Override
    public List<TbRechargeableCardProcess> selectTbRechargeableCardProcessList(TbRechargeableCardProcess tbRechargeableCardProcess) {
        return tbRechargeableCardProcessMapper.selectTbRechargeableCardProcessList(tbRechargeableCardProcess);
    }

    /**
     * 新增充值卡详情
     *
     * @param tbRechargeableCardProcess 充值卡详情
     * @return 结果
     */
    @Override
    public int insertTbRechargeableCardProcess(TbRechargeableCardProcess tbRechargeableCardProcess) {
        Long userId = SecurityUtils.getUserId();
        tbRechargeableCardProcess.setCreateBy(userId + "");
        tbRechargeableCardProcess.setUpdateTime(DateTime.now());
        tbRechargeableCardProcess.setUpdateBy(userId + "");
        tbRechargeableCardProcess.setCreateTime(DateUtils.getNowDate());
        return tbRechargeableCardProcessMapper.insertTbRechargeableCardProcess(tbRechargeableCardProcess);
    }

    /**
     * 修改充值卡详情
     *
     * @param tbRechargeableCardProcess 充值卡详情
     * @return 结果
     */
    @Override
    public int updateTbRechargeableCardProcess(TbRechargeableCardProcess tbRechargeableCardProcess) {

        TbRechargeableCardProcess tbRechargeableCardProcess1 = this.selectTbRechargeableCardProcessById(tbRechargeableCardProcess.getId());
        if (tbRechargeableCardProcess1.getUserId() != SecurityUtils.getUserId()) {
            return 0;
        }

        tbRechargeableCardProcess.setUpdateTime(DateUtils.getNowDate());
        return tbRechargeableCardProcessMapper.updateTbRechargeableCardProcess(tbRechargeableCardProcess);
    }

    /**
     * 批量删除充值卡详情
     *
     * @param ids 需要删除的充值卡详情主键
     * @return 结果
     */
    @Override
    public int deleteTbRechargeableCardProcessByIds(Long[] ids) {
        int count = 0;
        for (int i = 0; i < ids.length; i++) {
            TbRechargeableCardProcess tbRechargeableCardProcess = this.selectTbRechargeableCardProcessById(ids[i]);
            if (!Objects.isNull(tbRechargeableCardProcess)) {
                if (tbRechargeableCardProcess.getUserId() != SecurityUtils.getUserId()) {
                    return 0;
                }
            }
        }


        return tbRechargeableCardProcessMapper.deleteTbRechargeableCardProcessByIds(ids);
    }

    /**
     * 删除充值卡详情信息
     *
     * @param id 充值卡详情主键
     * @return 结果
     */
    @Override
    public int deleteTbRechargeableCardProcessById(Long id) {
        TbRechargeableCardProcess tbRechargeableCardProcessEqId = this.selectTbRechargeableCardProcessById(id);
        if (tbRechargeableCardProcessEqId.getUserId() != SecurityUtils.getUserId()) {
            return 0;
        }
        return tbRechargeableCardProcessMapper.deleteTbRechargeableCardProcessById(id);
    }


    /**
     * 查询充值卡详情  -----   管理员使用
     *
     * @param id 充值卡详情主键
     * @return 充值卡详情
     */
    @Override
    public TbRechargeableCardProcess selectTbRechargeableCardProcessByIdsys(Long id) {
        return tbRechargeableCardProcessMapper.selectTbRechargeableCardProcessById(id);
    }

    /**
     * 查询充值卡详情列表  -----   管理员使用
     *
     * @param tbRechargeableCardProcess 充值卡详情
     * @return 充值卡详情
     */
    @Override
    @Transactional
    public List<TbRechargeableCardProcess> selectTbRechargeableCardProcessListsys(TbRechargeableCardProcess tbRechargeableCardProcess) {
        return tbRechargeableCardProcessMapper.selectTbRechargeableCardProcessList(tbRechargeableCardProcess);
    }

    @Transactional
    public void creatCardNumber(TbRechargeableCardProcess tbRechargeableCardProcess) {
        //生成批次Id
        Long bathId = TimeUtil.timestampMedthod(DateTime.now()) / 1000;
        TbRechargeableCardMain tbRechargeableCardMain = new TbRechargeableCardMain();
        tbRechargeableCardMain.setUserId(SecurityUtils.getUserId());
        tbRechargeableCardMain.setBatchId(bathId);
        tbRechargeableCardMain.setIsDetele(0);
        tbRechargeableCardMain.setCreateBy(SecurityUtils.getUserId() + "");
        tbRechargeableCardMain.setCreateTime(DateTime.now());
        tbRechargeableCardMain.setUpdateBy(SecurityUtils.getUserId() + "");
        tbRechargeableCardMain.setUpdateTime(DateTime.now());
        tbRechargeableCardMain.setRemark(tbRechargeableCardProcess.getRemark());
        int i1 = iTbRechargeableCardMainService.insertTbRechargeableCardMainsys(tbRechargeableCardMain);
        if (i1 != 1) {
            throw new RuntimeException("生成失败");
        }
        tbRechargeableCardProcess.setBatchId(bathId);
        for (int i = 0; i < tbRechargeableCardProcess.getCreatNumber(); i++) {
            singCreatRadome(tbRechargeableCardProcess);
        }
    }

    /**
     * 生成单个
     */
    @Transactional
    public void singCreatRadome(TbRechargeableCardProcess tbRechargeableCardProcessParam) {
        TbRechargeableCardProcess tbRechargeableCardProcess = new TbRechargeableCardProcess();

        tbRechargeableCardProcess.setCardType(tbRechargeableCardProcessParam.getCardType());
        tbRechargeableCardProcess.setAddDate(tbRechargeableCardProcessParam.getAddDate());
        tbRechargeableCardProcess.setAddNum(tbRechargeableCardProcessParam.getAddNum());

        tbRechargeableCardProcess.setCardNumber(IdUtil.simpleUUID());
        tbRechargeableCardProcess.setBatchId(tbRechargeableCardProcessParam.getBatchId());
        tbRechargeableCardProcess.setIsDetele(0);
        tbRechargeableCardProcess.setCreateBy(SecurityUtils.getUserId() + "");
        tbRechargeableCardProcess.setCreateTime(DateTime.now());
        tbRechargeableCardProcess.setUpdateBy(SecurityUtils.getUserId() + "");
        tbRechargeableCardProcess.setUpdateTime(DateTime.now());
        int i = this.insertTbRechargeableCardProcess(tbRechargeableCardProcess);
        if (i != 1) {
            throw new RuntimeException("生成失败");
        }
    }

    /**
     * 新增充值卡详情  -----   管理员使用
     *
     * @param tbRechargeableCardProcess 充值卡详情
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTbRechargeableCardProcesssys(TbRechargeableCardProcess tbRechargeableCardProcess) {
        this.addBefore(tbRechargeableCardProcess);
        //如果激活卡为空,则说明位自动生成
        if (StrUtil.isBlank(tbRechargeableCardProcess.getCardNumber())) {
            creatCardNumber(tbRechargeableCardProcess);
        } else if (!Objects.isNull(tbRechargeableCardProcess.getBatchId())) {
            //如果激活卡不为空,则说明用户自己添加了一条
            TbRechargeableCardProcess tbRechargeableCardProcessInsert = new TbRechargeableCardProcess();
            tbRechargeableCardProcessInsert.setCardNumber(tbRechargeableCardProcess.getCardNumber());
            tbRechargeableCardProcessInsert.setBatchId(tbRechargeableCardProcess.getBatchId());
            tbRechargeableCardProcessInsert.setIsDetele(0);
            tbRechargeableCardProcessInsert.setCardType(tbRechargeableCardProcess.getCardType());
            tbRechargeableCardProcessInsert.setAddDate(tbRechargeableCardProcess.getAddDate());
            tbRechargeableCardProcessInsert.setAddNum(tbRechargeableCardProcess.getAddNum());
            tbRechargeableCardProcessInsert.setCreateBy(SecurityUtils.getUserId() + "");
            tbRechargeableCardProcessInsert.setCreateTime(DateTime.now());
            tbRechargeableCardProcessInsert.setUpdateBy(SecurityUtils.getUserId() + "");
            tbRechargeableCardProcessInsert.setUpdateTime(DateTime.now());
            int i = tbRechargeableCardProcessMapper.insertTbRechargeableCardProcess(tbRechargeableCardProcessInsert);
            if (i != 1) {
                throw new RuntimeException("生成失败");
            }
        } else {
            throw new RuntimeException("生成失败");
        }
        return 1;
    }

    /**
     * 添加前的校验
     *
     * @param tbRechargeableCardProcess
     */
    public void addBefore(TbRechargeableCardProcess tbRechargeableCardProcess) {
        if (Objects.isNull(tbRechargeableCardProcess.getCardType())) {
            throw new RuntimeException("充值卡类型不可为空");
        }

        if (Objects.isNull(tbRechargeableCardProcess.getCreatNumber()) || tbRechargeableCardProcess.getCreatNumber() < 1) {
            throw new RuntimeException("生成张数不可为空.且在0-200之间");
        }

        if (tbRechargeableCardProcess.getCardType() == 1) {
            //次数卡
            if (Objects.isNull(tbRechargeableCardProcess.getAddNum()) || tbRechargeableCardProcess.getAddNum() < 1) {
                throw new RuntimeException("充值卡次数不可为空或小于1");
            }
            tbRechargeableCardProcess.setAddDate(0l);
        } else if (tbRechargeableCardProcess.getCardType() == 2) {
            //分钟数
            if (Objects.isNull(tbRechargeableCardProcess.getAddDate()) || tbRechargeableCardProcess.getAddDate() < 1) {
                throw new RuntimeException("充值卡分钟数不可为空或小于1");
            }
            tbRechargeableCardProcess.setAddNum(0l);
        } else {
            throw new RuntimeException("充值类型异常");
        }
    }

    /**
     * 修改充值卡详情  -----   管理员使用
     *
     * @param tbRechargeableCardProcess 充值卡详情
     * @return 结果
     */
    @Override
    public int updateTbRechargeableCardProcesssys(TbRechargeableCardProcess tbRechargeableCardProcess) {
        tbRechargeableCardProcess.setUpdateTime(DateUtils.getNowDate());
        return tbRechargeableCardProcessMapper.updateTbRechargeableCardProcess(tbRechargeableCardProcess);
    }

    /**
     * 批量删除充值卡详情  -----   管理员使用
     *
     * @param ids 需要删除的充值卡详情主键
     * @return 结果
     */
    @Override
    public int deleteTbRechargeableCardProcessByIdssys(Long[] ids) {
        return tbRechargeableCardProcessMapper.deleteTbRechargeableCardProcessByIds(ids);
    }

    /**
     * 删除充值卡详情信息
     *
     * @param id 充值卡详情主键
     * @return 结果
     */
    @Override
    public int deleteTbRechargeableCardProcessByIdsys(Long id) {
        return tbRechargeableCardProcessMapper.deleteTbRechargeableCardProcessById(id);
    }

    @Override
    public int deleteTbRechargeableCardProcessByBathIdsys(Long bathId) {
        return tbRechargeableCardProcessMapper.deleteTbRechargeableCardProcessByBathIdsys(bathId);
    }

    /**
     * 充值卡使用
     *
     * @param cardNumber
     * @return
     */
    @Override
    @Transactional
    public int rechargeableCardUse(String cardNumber) {
        //首先查询未使用的激活卡
        TbRechargeableCardProcess tbRechargeableCardProcess = new TbRechargeableCardProcess();
        tbRechargeableCardProcess.setCardNumber(cardNumber);
        tbRechargeableCardProcess.setIsDetele(0);
        List<TbRechargeableCardProcess> tbRechargeableCardProcesses = this.selectTbRechargeableCardProcessList(tbRechargeableCardProcess);
        if (CollectionUtil.isEmpty(tbRechargeableCardProcesses) || tbRechargeableCardProcesses.size() > 1) {
            throw new RuntimeException("激活卡错误");
        }
        //激活卡正确的的话,更新激活卡为使用状态
        TbRechargeableCardProcess tbRechargeableCardProcessUpdate = tbRechargeableCardProcesses.get(0);
        //设置用户ID
        tbRechargeableCardProcessUpdate.setUserId(SecurityUtils.getUserId());
        //设置为使用状态
        tbRechargeableCardProcessUpdate.setIsDetele(1);
        tbRechargeableCardProcessUpdate.setUpdateTime(DateTime.now());
        tbRechargeableCardProcessUpdate.setUpdateBy(SecurityUtils.getUserId() + "");
        //更新激活卡状态
        int i = tbRechargeableCardProcessMapper.updateTbRechargeableCardProcess(tbRechargeableCardProcessUpdate);
        if (i != 1) {
            throw new RuntimeException("激活失败");
        }

        SysUser sysUser = iSysUserService.selectUserById(SecurityUtils.getUserId());
        Integer succStatus = 0;
        //封装日志记录
        TbFrequencyConsumptionLog tbFrequencyConsumptionLog = new TbFrequencyConsumptionLog();
        if (tbRechargeableCardProcessUpdate.getCardType() == 1) {
            //增加用户次数
            succStatus = iExtendSysUserService.blanceAddNumChange(sysUser, Math.toIntExact(tbRechargeableCardProcessUpdate.getAddNum()));
            tbFrequencyConsumptionLog.setRemark(tbRechargeableCardProcessUpdate.getAddNum() + "");
            tbFrequencyConsumptionLog.setChangeContent("充值卡【" + tbRechargeableCardProcessUpdate.getCardNumber() + "】充值,增加" + tbRechargeableCardProcessUpdate.getAddNum() + "次问答次数。");
            tbFrequencyConsumptionLog.setOperationType(2l);
        } else if (tbRechargeableCardProcessUpdate.getCardType() == 2) {
            //增加用户次数
            succStatus = iExtendSysUserService.blanceAddDateChange(sysUser, Math.toIntExact(tbRechargeableCardProcessUpdate.getAddDate()));
            tbFrequencyConsumptionLog.setRemark(tbRechargeableCardProcessUpdate.getAddDate() + "");
            tbFrequencyConsumptionLog.setChangeContent("充值卡【" + tbRechargeableCardProcessUpdate.getCardNumber() + "】充值,增加" + tbRechargeableCardProcessUpdate.getAddDate() + "分钟问答次数。");
            tbFrequencyConsumptionLog.setOperationType(3l);
        }
        if (succStatus != 1) {
            throw new RuntimeException("激活失败");
        }
        tbFrequencyConsumptionLog.setOperationName("充值卡激活");
        tbFrequencyConsumptionLog.setOperationId(tbRechargeableCardProcessUpdate.getId() + "");
        int insertStatus = iTbFrequencyConsumptionLogService.insertTbFrequencyConsumptionLog(tbFrequencyConsumptionLog);
        if (insertStatus != 1) {
            throw new RuntimeException("激活失败");
        }
        return 1;
    }
}
