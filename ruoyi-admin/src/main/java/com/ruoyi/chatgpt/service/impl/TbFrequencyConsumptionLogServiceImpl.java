package com.ruoyi.chatgpt.service.impl;

import cn.hutool.core.date.DateTime;
import com.ruoyi.chatgpt.domain.TbFrequencyConsumptionLog;
import com.ruoyi.chatgpt.mapper.TbFrequencyConsumptionLogMapper;
import com.ruoyi.chatgpt.service.ITbFrequencyConsumptionLogService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 次数消耗日志Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-27
 */
@Service
public class TbFrequencyConsumptionLogServiceImpl implements ITbFrequencyConsumptionLogService
{
    @Autowired
    private TbFrequencyConsumptionLogMapper tbFrequencyConsumptionLogMapper;

    /**
     * 查询次数消耗日志
     *
     * @param id 次数消耗日志主键
     * @return 次数消耗日志
     */
    @Override
    public TbFrequencyConsumptionLog selectTbFrequencyConsumptionLogById(Long id)
    {
        return tbFrequencyConsumptionLogMapper.selectTbFrequencyConsumptionLogById(id);
    }

    /**
     * 查询次数消耗日志列表
     *
     * @param tbFrequencyConsumptionLog 次数消耗日志
     * @return 次数消耗日志
     */
    @Override
    public List<TbFrequencyConsumptionLog> selectTbFrequencyConsumptionLogList(TbFrequencyConsumptionLog tbFrequencyConsumptionLog)
    {
        return tbFrequencyConsumptionLogMapper.selectTbFrequencyConsumptionLogList(tbFrequencyConsumptionLog);
    }

    /**
     * 新增次数消耗日志
     *
     * @param tbFrequencyConsumptionLog 次数消耗日志
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTbFrequencyConsumptionLog(TbFrequencyConsumptionLog tbFrequencyConsumptionLog)
    {
        Long userId = SecurityUtils.getUserId();
        tbFrequencyConsumptionLog.setUserId(userId);
        tbFrequencyConsumptionLog.setCreateBy(userId+"");
        tbFrequencyConsumptionLog.setUpdateTime(DateTime.now());
        tbFrequencyConsumptionLog.setUpdateBy(userId+"");
        tbFrequencyConsumptionLog.setCreateTime(DateUtils.getNowDate());
        tbFrequencyConsumptionLog.setIsDetele(0);
        return tbFrequencyConsumptionLogMapper.insertTbFrequencyConsumptionLog(tbFrequencyConsumptionLog);
    }
    @Override
    @Transactional
    public int insertTbFrequencyConsumptionLogRegister(TbFrequencyConsumptionLog tbFrequencyConsumptionLog)
    {
        Long userId = tbFrequencyConsumptionLog.getUserId();
        tbFrequencyConsumptionLog.setUserId(userId);
        tbFrequencyConsumptionLog.setCreateBy(userId+"");
        tbFrequencyConsumptionLog.setUpdateTime(DateTime.now());
        tbFrequencyConsumptionLog.setUpdateBy(userId+"");
        tbFrequencyConsumptionLog.setCreateTime(DateUtils.getNowDate());
        tbFrequencyConsumptionLog.setIsDetele(0);
        return tbFrequencyConsumptionLogMapper.insertTbFrequencyConsumptionLog(tbFrequencyConsumptionLog);
    }
    /**
     * 修改次数消耗日志
     *
     * @param tbFrequencyConsumptionLog 次数消耗日志
     * @return 结果
     */
    @Override
    public int updateTbFrequencyConsumptionLog(TbFrequencyConsumptionLog tbFrequencyConsumptionLog)
    {

        TbFrequencyConsumptionLog tbFrequencyConsumptionLog1 = this.selectTbFrequencyConsumptionLogById(tbFrequencyConsumptionLog.getId());
        if (tbFrequencyConsumptionLog1.getUserId()!=SecurityUtils.getUserId()){
            return 0;
        }

        tbFrequencyConsumptionLog.setUpdateTime(DateUtils.getNowDate());
        return tbFrequencyConsumptionLogMapper.updateTbFrequencyConsumptionLog(tbFrequencyConsumptionLog);
    }

    /**
     * 批量删除次数消耗日志
     *
     * @param ids 需要删除的次数消耗日志主键
     * @return 结果
     */
    @Override
    public int deleteTbFrequencyConsumptionLogByIds(Long[] ids)
    {
        int count = 0;
        for (int i=0;i<ids.length;i++){
            TbFrequencyConsumptionLog tbFrequencyConsumptionLog = this.selectTbFrequencyConsumptionLogById(ids[i]);
            if (!Objects.isNull(tbFrequencyConsumptionLog)){
                if (tbFrequencyConsumptionLog.getUserId() != SecurityUtils.getUserId()){
                    return 0;
                }
            }
        }


        return tbFrequencyConsumptionLogMapper.deleteTbFrequencyConsumptionLogByIds(ids);
    }

    /**
     * 删除次数消耗日志信息
     *
     * @param id 次数消耗日志主键
     * @return 结果
     */
    @Override
    public int deleteTbFrequencyConsumptionLogById(Long id)
    {
    TbFrequencyConsumptionLog tbFrequencyConsumptionLogEqId = this.selectTbFrequencyConsumptionLogById(id);
    if (tbFrequencyConsumptionLogEqId.getUserId()!=SecurityUtils.getUserId()){
        return 0;
    }



        return tbFrequencyConsumptionLogMapper.deleteTbFrequencyConsumptionLogById(id);
    }























    /**
     * 查询次数消耗日志  -----   管理员使用
     *
     * @param id 次数消耗日志主键
     * @return 次数消耗日志
     */
    @Override
    public TbFrequencyConsumptionLog selectTbFrequencyConsumptionLogByIdsys(Long id)
    {
        return tbFrequencyConsumptionLogMapper.selectTbFrequencyConsumptionLogById(id);
    }

    /**
     * 查询次数消耗日志列表  -----   管理员使用
     *
     * @param tbFrequencyConsumptionLog 次数消耗日志
     * @return 次数消耗日志
     */
    @Override
    public List<TbFrequencyConsumptionLog> selectTbFrequencyConsumptionLogListsys(TbFrequencyConsumptionLog tbFrequencyConsumptionLog)
    {
        return tbFrequencyConsumptionLogMapper.selectTbFrequencyConsumptionLogList(tbFrequencyConsumptionLog);
    }

    /**
     * 新增次数消耗日志  -----   管理员使用
     *
     * @param tbFrequencyConsumptionLog 次数消耗日志
     * @return 结果
     */
    @Override
    public int insertTbFrequencyConsumptionLogsys(TbFrequencyConsumptionLog tbFrequencyConsumptionLog)
    {
                tbFrequencyConsumptionLog.setCreateTime(DateUtils.getNowDate());
            return tbFrequencyConsumptionLogMapper.insertTbFrequencyConsumptionLog(tbFrequencyConsumptionLog);
    }

    /**
     * 修改次数消耗日志  -----   管理员使用
     *
     * @param tbFrequencyConsumptionLog 次数消耗日志
     * @return 结果
     */
    @Override
    public int updateTbFrequencyConsumptionLogsys(TbFrequencyConsumptionLog tbFrequencyConsumptionLog)
    {
                tbFrequencyConsumptionLog.setUpdateTime(DateUtils.getNowDate());
        return tbFrequencyConsumptionLogMapper.updateTbFrequencyConsumptionLog(tbFrequencyConsumptionLog);
    }

    /**
     * 批量删除次数消耗日志  -----   管理员使用
     *
     * @param ids 需要删除的次数消耗日志主键
     * @return 结果
     */
    @Override
    public int deleteTbFrequencyConsumptionLogByIdssys(Long[] ids)
    {
        return tbFrequencyConsumptionLogMapper.deleteTbFrequencyConsumptionLogByIds(ids);
    }

    /**
     * 删除次数消耗日志信息
     *
     * @param id 次数消耗日志主键
     * @return 结果
     */
    @Override
    public int deleteTbFrequencyConsumptionLogByIdsys(Long id)
    {
        return tbFrequencyConsumptionLogMapper.deleteTbFrequencyConsumptionLogById(id);
    }


    @Override
    public int getIntivateCount(Long id)
    {
        return tbFrequencyConsumptionLogMapper.getIntivateCount(id);
    }

}
