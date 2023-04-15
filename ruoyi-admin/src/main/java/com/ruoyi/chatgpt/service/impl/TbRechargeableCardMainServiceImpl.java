package com.ruoyi.chatgpt.service.impl;

import cn.hutool.core.date.DateTime;
import com.ruoyi.chatgpt.domain.TbRechargeableCardMain;
import com.ruoyi.chatgpt.mapper.TbRechargeableCardMainMapper;
import com.ruoyi.chatgpt.service.ITbRechargeableCardMainService;
import com.ruoyi.chatgpt.service.ITbRechargeableCardProcessService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 充值卡-主Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-27
 */
@Service
public class TbRechargeableCardMainServiceImpl implements ITbRechargeableCardMainService
{
    @Autowired
    private TbRechargeableCardMainMapper tbRechargeableCardMainMapper;
    @Autowired
    private ITbRechargeableCardProcessService iTbRechargeableCardProcessService;

    /**
     * 查询充值卡-主
     *
     * @param id 充值卡-主主键
     * @return 充值卡-主
     */
    @Override
    public TbRechargeableCardMain selectTbRechargeableCardMainById(Long id)
    {
        return tbRechargeableCardMainMapper.selectTbRechargeableCardMainById(id);
    }

    /**
     * 查询充值卡-主列表
     *
     * @param tbRechargeableCardMain 充值卡-主
     * @return 充值卡-主
     */
    @Override
    public List<TbRechargeableCardMain> selectTbRechargeableCardMainList(TbRechargeableCardMain tbRechargeableCardMain)
    {
        return tbRechargeableCardMainMapper.selectTbRechargeableCardMainList(tbRechargeableCardMain);
    }

    /**
     * 新增充值卡-主
     *
     * @param tbRechargeableCardMain 充值卡-主
     * @return 结果
     */
    @Override
    public int insertTbRechargeableCardMain(TbRechargeableCardMain tbRechargeableCardMain)
    {

        //管理员批量生成
        Long userId = SecurityUtils.getUserId();
        tbRechargeableCardMain.setUserId(userId);
        tbRechargeableCardMain.setCreateBy(userId+"");
        tbRechargeableCardMain.setUpdateTime(DateTime.now());
        tbRechargeableCardMain.setUpdateBy(userId+"");
        tbRechargeableCardMain.setCreateTime(DateUtils.getNowDate());
        return tbRechargeableCardMainMapper.insertTbRechargeableCardMain(tbRechargeableCardMain);
    }

    /**
     * 修改充值卡-主
     *
     * @param tbRechargeableCardMain 充值卡-主
     * @return 结果
     */
    @Override
    public int updateTbRechargeableCardMain(TbRechargeableCardMain tbRechargeableCardMain)
    {

        TbRechargeableCardMain tbRechargeableCardMain1 = this.selectTbRechargeableCardMainById(tbRechargeableCardMain.getId());
        if (tbRechargeableCardMain1.getUserId()!=SecurityUtils.getUserId()){
            return 0;
        }

        tbRechargeableCardMain.setUpdateTime(DateUtils.getNowDate());
        return tbRechargeableCardMainMapper.updateTbRechargeableCardMain(tbRechargeableCardMain);
    }

    /**
     * 批量删除充值卡-主
     *
     * @param ids 需要删除的充值卡-主主键
     * @return 结果
     */
    @Override
    public int deleteTbRechargeableCardMainByIds(Long[] ids)
    {
        int count = 0;
        for (int i=0;i<ids.length;i++){
            TbRechargeableCardMain tbRechargeableCardMain = this.selectTbRechargeableCardMainById(ids[i]);
            if (!Objects.isNull(tbRechargeableCardMain)){
                if (tbRechargeableCardMain.getUserId() != SecurityUtils.getUserId()){
                    return 0;
                }
            }
        }


        return tbRechargeableCardMainMapper.deleteTbRechargeableCardMainByIds(ids);
    }

    /**
     * 删除充值卡-主信息
     *
     * @param id 充值卡-主主键
     * @return 结果
     */
    @Override
    public int deleteTbRechargeableCardMainById(Long id)
    {
    TbRechargeableCardMain tbRechargeableCardMainEqId = this.selectTbRechargeableCardMainById(id);
    if (tbRechargeableCardMainEqId.getUserId()!=SecurityUtils.getUserId()){
        return 0;
    }



        return tbRechargeableCardMainMapper.deleteTbRechargeableCardMainById(id);
    }























    /**
     * 查询充值卡-主  -----   管理员使用
     *
     * @param id 充值卡-主主键
     * @return 充值卡-主
     */
    @Override
    public TbRechargeableCardMain selectTbRechargeableCardMainByIdsys(Long id)
    {
        return tbRechargeableCardMainMapper.selectTbRechargeableCardMainById(id);
    }

    /**
     * 查询充值卡-主列表  -----   管理员使用
     *
     * @param tbRechargeableCardMain 充值卡-主
     * @return 充值卡-主
     */
    @Override
    public List<TbRechargeableCardMain> selectTbRechargeableCardMainListsys(TbRechargeableCardMain tbRechargeableCardMain)
    {
        return tbRechargeableCardMainMapper.selectTbRechargeableCardMainList(tbRechargeableCardMain);
    }

    /**
     * 新增充值卡-主  -----   管理员使用
     *
     * @param tbRechargeableCardMain 充值卡-主
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTbRechargeableCardMainsys(TbRechargeableCardMain tbRechargeableCardMain)
    {
        tbRechargeableCardMain.setCreateTime(DateUtils.getNowDate());
        return tbRechargeableCardMainMapper.insertTbRechargeableCardMain(tbRechargeableCardMain);
    }

    /**
     * 修改充值卡-主  -----   管理员使用
     *
     * @param tbRechargeableCardMain 充值卡-主
     * @return 结果
     */
    @Override
    public int updateTbRechargeableCardMainsys(TbRechargeableCardMain tbRechargeableCardMain)
    {
                tbRechargeableCardMain.setUpdateTime(DateUtils.getNowDate());
        return tbRechargeableCardMainMapper.updateTbRechargeableCardMain(tbRechargeableCardMain);
    }

    /**
     * 批量删除充值卡-主  -----   管理员使用
     *
     * @param ids 需要删除的充值卡-主主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTbRechargeableCardMainByIdssys(Long[] ids)
    {
        if (!Objects.isNull(ids) && ids.length>0){
            for (int i =0;i<ids.length;i++){
                TbRechargeableCardMain tbRechargeableCardMain = this.selectTbRechargeableCardMainById(ids[0]);
                if (Objects.isNull(tbRechargeableCardMain)){
                    throw new RuntimeException("删除失败");
                }
                Long batchId = tbRechargeableCardMain.getBatchId();
                iTbRechargeableCardProcessService.deleteTbRechargeableCardProcessByBathIdsys(batchId);
            }

        }
        return tbRechargeableCardMainMapper.deleteTbRechargeableCardMainByIds(ids);
    }

    /**
     * 删除充值卡-主信息
     *
     * @param id 充值卡-主主键
     * @return 结果
     */
    @Override
    public int deleteTbRechargeableCardMainByIdsys(Long id)
    {
        return tbRechargeableCardMainMapper.deleteTbRechargeableCardMainById(id);
    }
    @Override
    @Transactional
    public int deleteTbRechargeableCardMainByBathIdsys(Long bathId)
    {
        int i = tbRechargeableCardMainMapper.deleteTbRechargeableCardMainByBathIdsys(bathId);

        int i1 = iTbRechargeableCardProcessService.deleteTbRechargeableCardProcessByBathIdsys(bathId);
//        //删除主表
//        if (i<1 || i1<1){
//            throw new RuntimeException("请刷新后重试");
//        }
        return i;
    }


}
