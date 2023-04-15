package com.ruoyi.chatgpt.service;

import com.ruoyi.chatgpt.domain.TbFrequencyConsumptionLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 次数消耗日志Service接口
 *
 * @author ruoyi
 * @date 2023-03-27
 */
public interface ITbFrequencyConsumptionLogService
{
    /**
     * 查询次数消耗日志
     *
     * @param id 次数消耗日志主键
     * @return 次数消耗日志
     */
    public TbFrequencyConsumptionLog selectTbFrequencyConsumptionLogById(Long id);

    /**
     * 查询次数消耗日志列表
     *
     * @param tbFrequencyConsumptionLog 次数消耗日志
     * @return 次数消耗日志集合
     */
    public List<TbFrequencyConsumptionLog> selectTbFrequencyConsumptionLogList(TbFrequencyConsumptionLog tbFrequencyConsumptionLog);

    /**
     * 新增次数消耗日志
     *
     * @param tbFrequencyConsumptionLog 次数消耗日志
     * @return 结果
     */
    public int insertTbFrequencyConsumptionLog(TbFrequencyConsumptionLog tbFrequencyConsumptionLog);
    public int  insertTbFrequencyConsumptionLogRegister(TbFrequencyConsumptionLog tbFrequencyConsumptionLog);
    /**
     * 修改次数消耗日志
     *
     * @param tbFrequencyConsumptionLog 次数消耗日志
     * @return 结果
     */
    public int updateTbFrequencyConsumptionLog(TbFrequencyConsumptionLog tbFrequencyConsumptionLog);

    /**
     * 批量删除次数消耗日志
     *
     * @param ids 需要删除的次数消耗日志主键集合
     * @return 结果
     */
    public int deleteTbFrequencyConsumptionLogByIds(Long[] ids);

    /**
     * 删除次数消耗日志信息
     *
     * @param id 次数消耗日志主键
     * @return 结果
     */
    public int deleteTbFrequencyConsumptionLogById(Long id);









    /**
     * 查询次数消耗日志  -----   管理员使用
     *
     * @param id 次数消耗日志主键
     * @return 次数消耗日志
     */
    public TbFrequencyConsumptionLog selectTbFrequencyConsumptionLogByIdsys(Long id);

    /**
     * 查询次数消耗日志列表  -----   管理员使用
     *
     * @param tbFrequencyConsumptionLog 次数消耗日志
     * @return 次数消耗日志集合
     */
    public List<TbFrequencyConsumptionLog> selectTbFrequencyConsumptionLogListsys(TbFrequencyConsumptionLog tbFrequencyConsumptionLog);

    /**
     * 新增次数消耗日志  -----   管理员使用
     *
     * @param tbFrequencyConsumptionLog 次数消耗日志
     * @return 结果
     */
    public int insertTbFrequencyConsumptionLogsys(TbFrequencyConsumptionLog tbFrequencyConsumptionLog);

    /**
     * 修改次数消耗日志  -----   管理员使用
     *
     * @param tbFrequencyConsumptionLog 次数消耗日志
     * @return 结果
     */
    public int updateTbFrequencyConsumptionLogsys(TbFrequencyConsumptionLog tbFrequencyConsumptionLog);

    /**
     * 批量删除次数消耗日志  -----   管理员使用
     *
     * @param ids 需要删除的次数消耗日志主键集合
     * @return 结果
     */
    public int deleteTbFrequencyConsumptionLogByIdssys(Long[] ids);

    /**
     * 删除次数消耗日志信息  -----   管理员使用
     *
     * @param id 次数消耗日志主键
     * @return 结果
     */
    public int deleteTbFrequencyConsumptionLogByIdsys(Long id);

    public int getIntivateCount(Long userId);
}
