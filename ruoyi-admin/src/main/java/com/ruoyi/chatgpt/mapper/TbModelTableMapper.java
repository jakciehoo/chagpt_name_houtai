package com.ruoyi.chatgpt.mapper;

import java.util.List;
import com.ruoyi.chatgpt.domain.TbModelTable;

/**
 * 模型建设Mapper接口
 *
 * @author zhx
 * @date 2023-04-11
 */
public interface TbModelTableMapper
{
    /**
     * 查询模型建设
     *
     * @param id 模型建设主键
     * @return 模型建设
     */
    public TbModelTable selectTbModelTableById(Long id);

    /**
     * 查询模型建设列表
     *
     * @param tbModelTable 模型建设
     * @return 模型建设集合
     */
    public List<TbModelTable> selectTbModelTableList(TbModelTable tbModelTable);

    /**
     * 新增模型建设
     *
     * @param tbModelTable 模型建设
     * @return 结果
     */
    public int insertTbModelTable(TbModelTable tbModelTable);

    /**
     * 修改模型建设
     *
     * @param tbModelTable 模型建设
     * @return 结果
     */
    public int updateTbModelTable(TbModelTable tbModelTable);

    /**
     * 删除模型建设
     *
     * @param id 模型建设主键
     * @return 结果
     */
    public int deleteTbModelTableById(Long id);

    /**
     * 批量删除模型建设
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbModelTableByIds(Long[] ids);

    public List<TbModelTable> selectTbModelListBydRoleId(Long dRoleId);
}
