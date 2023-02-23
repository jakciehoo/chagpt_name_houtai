package com.ruoyi.chatgpt.service;

import java.util.List;
import com.ruoyi.chatgpt.domain.TbAnsweEmploy;

/**
 * 回答收录列Service接口
 *
 * @author ruoyi
 * @date 2023-02-10
 */
public interface ITbAnsweEmployService
{
    /**
     * 查询回答收录列
     *
     * @param anserId 回答收录列主键
     * @return 回答收录列
     */
    public TbAnsweEmploy selectTbAnsweEmployByAnserId(Long anserId);

    /**
     * 查询回答收录列列表
     *
     * @param tbAnsweEmploy 回答收录列
     * @return 回答收录列集合
     */
    public List<TbAnsweEmploy> selectTbAnsweEmployList(TbAnsweEmploy tbAnsweEmploy);


    /**
     * 新增回答收录列 --   自动收入
     *
     * @param tbAnsweEmploy 回答收录列 --   自动收入
     * @return 结果
     */
    public int insertTbAnsweEmployAuto(TbAnsweEmploy tbAnsweEmploy);


    /**
     * 新增回答收录列
     *
     * @param tbAnsweEmploy 回答收录列
     * @return 结果
     */
    public int insertTbAnsweEmploy(TbAnsweEmploy tbAnsweEmploy);

    /**
     * 修改回答收录列
     *
     * @param tbAnsweEmploy 回答收录列
     * @return 结果
     */
    public int updateTbAnsweEmploy(TbAnsweEmploy tbAnsweEmploy);

    /**
     * 批量删除回答收录列
     *
     * @param anserIds 需要删除的回答收录列主键集合
     * @return 结果
     */
    public int deleteTbAnsweEmployByAnserIds(Long[] anserIds);

    /**
     * 删除回答收录列信息
     *
     * @param anserId 回答收录列主键
     * @return 结果
     */
    public int deleteTbAnsweEmployByAnserId(Long anserId);









    /**
     * 查询回答收录列  -----   管理员使用
     *
     * @param anserId 回答收录列主键
     * @return 回答收录列
     */
    public TbAnsweEmploy selectTbAnsweEmployByAnserIdsys(Long anserId);

    /**
     * 查询回答收录列列表  -----   管理员使用
     *
     * @param tbAnsweEmploy 回答收录列
     * @return 回答收录列集合
     */
    public List<TbAnsweEmploy> selectTbAnsweEmployListsys(TbAnsweEmploy tbAnsweEmploy);

    /**
     * 新增回答收录列  -----   管理员使用
     *
     * @param tbAnsweEmploy 回答收录列
     * @return 结果
     */
    public int insertTbAnsweEmploysys(TbAnsweEmploy tbAnsweEmploy);

    /**
     * 修改回答收录列  -----   管理员使用
     *
     * @param tbAnsweEmploy 回答收录列
     * @return 结果
     */
    public int updateTbAnsweEmploysys(TbAnsweEmploy tbAnsweEmploy);

    /**
     * 批量删除回答收录列  -----   管理员使用
     *
     * @param anserIds 需要删除的回答收录列主键集合
     * @return 结果
     */
    public int deleteTbAnsweEmployByAnserIdssys(Long[] anserIds);

    /**
     * 删除回答收录列信息  -----   管理员使用
     *
     * @param anserId 回答收录列主键
     * @return 结果
     */
    public int deleteTbAnsweEmployByAnserIdsys(Long anserId);
}
