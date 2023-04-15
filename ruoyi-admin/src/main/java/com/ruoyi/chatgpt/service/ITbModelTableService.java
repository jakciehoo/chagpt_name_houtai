package com.ruoyi.chatgpt.service;

import java.util.List;
import com.ruoyi.chatgpt.domain.TbModelTable;

/**
 * 模型建设Service接口
 *
 * @author zhx
 * @date 2023-04-11
 */
public interface ITbModelTableService
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
     * 批量删除模型建设
     *
     * @param ids 需要删除的模型建设主键集合
     * @return 结果
     */
    public int deleteTbModelTableByIds(Long[] ids);

    /**
     * 删除模型建设信息
     *
     * @param id 模型建设主键
     * @return 结果
     */
    public int deleteTbModelTableById(Long id);









    /**
     * 查询模型建设  -----   管理员使用
     *
     * @param id 模型建设主键
     * @return 模型建设
     */
    public TbModelTable selectTbModelTableByIdsys(Long id);

    /**
     * 查询模型建设列表  -----   管理员使用
     *
     * @param tbModelTable 模型建设
     * @return 模型建设集合
     */
    public List<TbModelTable> selectTbModelTableListsys(TbModelTable tbModelTable);

    /**
     * 新增模型建设  -----   管理员使用
     *
     * @param tbModelTable 模型建设
     * @return 结果
     */
    public int insertTbModelTablesys(TbModelTable tbModelTable);

    /**
     * 修改模型建设  -----   管理员使用
     *
     * @param tbModelTable 模型建设
     * @return 结果
     */
    public int updateTbModelTablesys(TbModelTable tbModelTable);

    /**
     * 批量删除模型建设  -----   管理员使用
     *
     * @param ids 需要删除的模型建设主键集合
     * @return 结果
     */
    public int deleteTbModelTableByIdssys(Long[] ids);

    /**
     * 删除模型建设信息  -----   管理员使用
     *
     * @param id 模型建设主键
     * @return 结果
     */
    public int deleteTbModelTableByIdsys(Long id);




    /**
     * 通过模型ID查询list
     *
     * @param dRoleId 模型建设
     * @return 模型建设集合
     */
    public List<TbModelTable> selectTbModelListBydRoleId(Long dRoleId);

}
