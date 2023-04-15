package com.ruoyi.chatgpt.service;

import com.ruoyi.chatgpt.domain.TbRechargeableCardProcess;

import java.util.List;

/**
 * 充值卡详情Service接口
 *
 * @author zhx
 * @date 2023-03-27
 */
public interface ITbRechargeableCardProcessService
{
    /**
     * 查询充值卡详情
     *
     * @param id 充值卡详情主键
     * @return 充值卡详情
     */
    public TbRechargeableCardProcess selectTbRechargeableCardProcessById(Long id);

    /**
     * 查询充值卡详情列表
     *
     * @param tbRechargeableCardProcess 充值卡详情
     * @return 充值卡详情集合
     */
    public List<TbRechargeableCardProcess> selectTbRechargeableCardProcessList(TbRechargeableCardProcess tbRechargeableCardProcess);

    /**
     * 新增充值卡详情
     *
     * @param tbRechargeableCardProcess 充值卡详情
     * @return 结果
     */
    public int insertTbRechargeableCardProcess(TbRechargeableCardProcess tbRechargeableCardProcess);

    /**
     * 修改充值卡详情
     *
     * @param tbRechargeableCardProcess 充值卡详情
     * @return 结果
     */
    public int updateTbRechargeableCardProcess(TbRechargeableCardProcess tbRechargeableCardProcess);

    /**
     * 批量删除充值卡详情
     *
     * @param ids 需要删除的充值卡详情主键集合
     * @return 结果
     */
    public int deleteTbRechargeableCardProcessByIds(Long[] ids);

    /**
     * 删除充值卡详情信息
     *
     * @param id 充值卡详情主键
     * @return 结果
     */
    public int deleteTbRechargeableCardProcessById(Long id);









    /**
     * 查询充值卡详情  -----   管理员使用
     *
     * @param id 充值卡详情主键
     * @return 充值卡详情
     */
    public TbRechargeableCardProcess selectTbRechargeableCardProcessByIdsys(Long id);

    /**
     * 查询充值卡详情列表  -----   管理员使用
     *
     * @param tbRechargeableCardProcess 充值卡详情
     * @return 充值卡详情集合
     */
    public List<TbRechargeableCardProcess> selectTbRechargeableCardProcessListsys(TbRechargeableCardProcess tbRechargeableCardProcess);

    /**
     * 新增充值卡详情  -----   管理员使用
     *
     * @param tbRechargeableCardProcess 充值卡详情
     * @return 结果
     */
    public int insertTbRechargeableCardProcesssys(TbRechargeableCardProcess tbRechargeableCardProcess);

    /**
     * 修改充值卡详情  -----   管理员使用
     *
     * @param tbRechargeableCardProcess 充值卡详情
     * @return 结果
     */
    public int updateTbRechargeableCardProcesssys(TbRechargeableCardProcess tbRechargeableCardProcess);

    /**
     * 批量删除充值卡详情  -----   管理员使用
     *
     * @param ids 需要删除的充值卡详情主键集合
     * @return 结果
     */
    public int deleteTbRechargeableCardProcessByIdssys(Long[] ids);

    /**
     * 删除充值卡详情信息  -----   管理员使用
     *
     * @param id 充值卡详情主键
     * @return 结果
     */
    public int deleteTbRechargeableCardProcessByIdsys(Long id);

    public int deleteTbRechargeableCardProcessByBathIdsys(Long bathId);

    public int  rechargeableCardUse(String cardNumber);

}
