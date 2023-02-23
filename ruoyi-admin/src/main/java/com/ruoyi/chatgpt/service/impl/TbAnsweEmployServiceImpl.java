package com.ruoyi.chatgpt.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.chatgpt.mapper.TbAnsweEmployMapper;
import com.ruoyi.chatgpt.domain.TbAnsweEmploy;
import com.ruoyi.chatgpt.service.ITbAnsweEmployService;
import java.util.Objects;
import com.ruoyi.common.utils.SecurityUtils;
import cn.hutool.core.date.DateTime;
/**
 * 回答收录列Service业务层处理
 *
 * @author ruoyi
 * @date 2023-02-10
 */
@Service
public class TbAnsweEmployServiceImpl implements ITbAnsweEmployService
{
    @Autowired
    private TbAnsweEmployMapper tbAnsweEmployMapper;

    /**
     * 查询回答收录列
     *
     * @param anserId 回答收录列主键
     * @return 回答收录列
     */
    @Override
    public TbAnsweEmploy selectTbAnsweEmployByAnserId(Long anserId)
    {
        return tbAnsweEmployMapper.selectTbAnsweEmployByAnserId(anserId);
    }

    /**
     * 查询回答收录列列表
     *
     * @param tbAnsweEmploy 回答收录列
     * @return 回答收录列
     */
    @Override
    public List<TbAnsweEmploy> selectTbAnsweEmployList(TbAnsweEmploy tbAnsweEmploy)
    {
        return tbAnsweEmployMapper.selectTbAnsweEmployList(tbAnsweEmploy);
    }

    /**
     * 新增回答收录列
     *
     * @param tbAnsweEmploy 回答收录列
     * @return 结果
     */
    @Override
    public int insertTbAnsweEmploy(TbAnsweEmploy tbAnsweEmploy)
    {
        Long userId = SecurityUtils.getUserId();
        tbAnsweEmploy.setUserId(userId);
        tbAnsweEmploy.setCreateBy(userId+"");
        tbAnsweEmploy.setUpdateTime(DateTime.now());
        tbAnsweEmploy.setUpdateBy(userId+"");
        tbAnsweEmploy.setCreateTime(DateUtils.getNowDate());
        return tbAnsweEmployMapper.insertTbAnsweEmploy(tbAnsweEmploy);
    }




    /**
     * 新增回答收录列
     *
     * @param tbAnsweEmploy 回答收录列  --  自动插入
     * @return 结果
     */
    @Override
    public int insertTbAnsweEmployAuto(TbAnsweEmploy tbAnsweEmploy)
    {
        tbAnsweEmploy.setIsShow(0l);
        tbAnsweEmploy.setUserId(-1l);
        tbAnsweEmploy.setCreateBy("-1");
        tbAnsweEmploy.setUpdateTime(DateTime.now());
        tbAnsweEmploy.setUpdateBy("-1");
        tbAnsweEmploy.setCreateTime(DateUtils.getNowDate());
        return tbAnsweEmployMapper.insertTbAnsweEmploy(tbAnsweEmploy);
    }



    /**
     * 修改回答收录列
     *
     * @param tbAnsweEmploy 回答收录列
     * @return 结果
     */
    @Override
    public int updateTbAnsweEmploy(TbAnsweEmploy tbAnsweEmploy)
    {

        TbAnsweEmploy tbAnsweEmploy1 = this.selectTbAnsweEmployByAnserId(tbAnsweEmploy.getAnserId());
        if (tbAnsweEmploy1.getUserId()!=SecurityUtils.getUserId()){
            return 0;
        }

        tbAnsweEmploy.setUpdateTime(DateUtils.getNowDate());
        return tbAnsweEmployMapper.updateTbAnsweEmploy(tbAnsweEmploy);
    }

    /**
     * 批量删除回答收录列
     *
     * @param anserIds 需要删除的回答收录列主键
     * @return 结果
     */
    @Override
    public int deleteTbAnsweEmployByAnserIds(Long[] anserIds)
    {
        int count = 0;
        for (int i=0;i<anserIds.length;i++){
            TbAnsweEmploy tbAnsweEmploy = this.selectTbAnsweEmployByAnserId(anserIds[i]);
            if (!Objects.isNull(tbAnsweEmploy)){
                if (tbAnsweEmploy.getUserId() != SecurityUtils.getUserId()){
                    return 0;
                }
            }
        }


        return tbAnsweEmployMapper.deleteTbAnsweEmployByAnserIds(anserIds);
    }

    /**
     * 删除回答收录列信息
     *
     * @param anserId 回答收录列主键
     * @return 结果
     */
    @Override
    public int deleteTbAnsweEmployByAnserId(Long anserId)
    {
    TbAnsweEmploy tbAnsweEmployEqId = this.selectTbAnsweEmployByAnserId(anserId);
    if (tbAnsweEmployEqId.getUserId()!=SecurityUtils.getUserId()){
        return 0;
    }



        return tbAnsweEmployMapper.deleteTbAnsweEmployByAnserId(anserId);
    }























    /**
     * 查询回答收录列  -----   管理员使用
     *
     * @param anserId 回答收录列主键
     * @return 回答收录列
     */
    @Override
    public TbAnsweEmploy selectTbAnsweEmployByAnserIdsys(Long anserId)
    {
        return tbAnsweEmployMapper.selectTbAnsweEmployByAnserId(anserId);
    }

    /**
     * 查询回答收录列列表  -----   管理员使用
     *
     * @param tbAnsweEmploy 回答收录列
     * @return 回答收录列
     */
    @Override
    public List<TbAnsweEmploy> selectTbAnsweEmployListsys(TbAnsweEmploy tbAnsweEmploy)
    {
        return tbAnsweEmployMapper.selectTbAnsweEmployList(tbAnsweEmploy);
    }

    /**
     * 新增回答收录列  -----   管理员使用
     *
     * @param tbAnsweEmploy 回答收录列
     * @return 结果
     */
    @Override
    public int insertTbAnsweEmploysys(TbAnsweEmploy tbAnsweEmploy)
    {
                tbAnsweEmploy.setCreateTime(DateUtils.getNowDate());
            return tbAnsweEmployMapper.insertTbAnsweEmploy(tbAnsweEmploy);
    }

    /**
     * 修改回答收录列  -----   管理员使用
     *
     * @param tbAnsweEmploy 回答收录列
     * @return 结果
     */
    @Override
    public int updateTbAnsweEmploysys(TbAnsweEmploy tbAnsweEmploy)
    {
                tbAnsweEmploy.setUpdateTime(DateUtils.getNowDate());
        return tbAnsweEmployMapper.updateTbAnsweEmploy(tbAnsweEmploy);
    }

    /**
     * 批量删除回答收录列  -----   管理员使用
     *
     * @param anserIds 需要删除的回答收录列主键
     * @return 结果
     */
    @Override
    public int deleteTbAnsweEmployByAnserIdssys(Long[] anserIds)
    {
        return tbAnsweEmployMapper.deleteTbAnsweEmployByAnserIds(anserIds);
    }

    /**
     * 删除回答收录列信息
     *
     * @param anserId 回答收录列主键
     * @return 结果
     */
    @Override
    public int deleteTbAnsweEmployByAnserIdsys(Long anserId)
    {
        return tbAnsweEmployMapper.deleteTbAnsweEmployByAnserId(anserId);
    }



}
