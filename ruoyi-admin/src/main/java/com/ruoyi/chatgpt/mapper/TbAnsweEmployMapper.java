package com.ruoyi.chatgpt.mapper;

import java.util.List;
import com.ruoyi.chatgpt.domain.TbAnsweEmploy;

/**
 * 回答收录列Mapper接口
 *
 * @author ruoyi
 * @date 2023-02-10
 */
public interface TbAnsweEmployMapper
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
     * 删除回答收录列
     *
     * @param anserId 回答收录列主键
     * @return 结果
     */
    public int deleteTbAnsweEmployByAnserId(Long anserId);

    /**
     * 批量删除回答收录列
     *
     * @param anserIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbAnsweEmployByAnserIds(Long[] anserIds);
}
