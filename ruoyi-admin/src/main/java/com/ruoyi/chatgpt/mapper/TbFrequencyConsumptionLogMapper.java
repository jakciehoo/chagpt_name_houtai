package com.ruoyi.chatgpt.mapper;

import com.ruoyi.chatgpt.domain.TbFrequencyConsumptionLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 次数消耗日志Mapper接口
 *
 * @author ruoyi
 * @date 2023-03-27
 */
public interface TbFrequencyConsumptionLogMapper
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

    /**
     * 修改次数消耗日志
     *
     * @param tbFrequencyConsumptionLog 次数消耗日志
     * @return 结果
     */
    public int updateTbFrequencyConsumptionLog(TbFrequencyConsumptionLog tbFrequencyConsumptionLog);

    /**
     * 删除次数消耗日志
     *
     * @param id 次数消耗日志主键
     * @return 结果
     */
    public int deleteTbFrequencyConsumptionLogById(Long id);

    /**
     * 批量删除次数消耗日志
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbFrequencyConsumptionLogByIds(Long[] ids);


    @Select("select  IFNULL(NULLIF(sum(remark), '' ), 0)  from tb_frequency_consumption_log where user_id=#{userId} and operation_type = 4")
    public int getIntivateCount(@Param("userId")Long userId);
}
