package com.ruoyi.chatgpt.mapper;

import com.ruoyi.chatgpt.domain.TbRechargeableCardMain;

import java.util.List;

/**
 * 充值卡-主Mapper接口
 *
 * @author ruoyi
 * @date 2023-03-27
 */
public interface TbRechargeableCardMainMapper
{
    /**
     * 查询充值卡-主
     *
     * @param id 充值卡-主主键
     * @return 充值卡-主
     */
    public TbRechargeableCardMain selectTbRechargeableCardMainById(Long id);

    /**
     * 查询充值卡-主列表
     *
     * @param tbRechargeableCardMain 充值卡-主
     * @return 充值卡-主集合
     */
    public List<TbRechargeableCardMain> selectTbRechargeableCardMainList(TbRechargeableCardMain tbRechargeableCardMain);

    /**
     * 新增充值卡-主
     *
     * @param tbRechargeableCardMain 充值卡-主
     * @return 结果
     */
    public int insertTbRechargeableCardMain(TbRechargeableCardMain tbRechargeableCardMain);

    /**
     * 修改充值卡-主
     *
     * @param tbRechargeableCardMain 充值卡-主
     * @return 结果
     */
    public int updateTbRechargeableCardMain(TbRechargeableCardMain tbRechargeableCardMain);

    /**
     * 删除充值卡-主
     *
     * @param id 充值卡-主主键
     * @return 结果
     */
    public int deleteTbRechargeableCardMainById(Long id);

    /**
     * 批量删除充值卡-主
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbRechargeableCardMainByIds(Long[] ids);

    public int deleteTbRechargeableCardMainByBathIdsys(Long bathId);

}
