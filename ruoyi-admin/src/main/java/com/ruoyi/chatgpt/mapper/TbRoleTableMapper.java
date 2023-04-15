package com.ruoyi.chatgpt.mapper;

import com.ruoyi.chatgpt.domain.TbRoleTable;

import java.util.List;

/**
 * 角色管理Mapper接口
 *
 * @author ruoyi
 * @date 2023-03-22
 */
public interface TbRoleTableMapper
{
    /**
     * 查询角色管理
     *
     * @param id 角色管理主键
     * @return 角色管理
     */
    TbRoleTable selectTbRoleTableById(Long id);

    TbRoleTable selectTbRoleTableByIdAndShow(TbRoleTable tbRoleTable);

    /**
     * 查询角色管理列表
     *
     * @param tbRoleTable 角色管理
     * @return 角色管理集合
     */
    List<TbRoleTable> selectTbRoleTableList(TbRoleTable tbRoleTable);

    /**
     * 新增角色管理
     *
     * @param tbRoleTable 角色管理
     * @return 结果
     */
    int insertTbRoleTable(TbRoleTable tbRoleTable);

    /**
     * 修改角色管理
     *
     * @param tbRoleTable 角色管理
     * @return 结果
     */
    int updateTbRoleTable(TbRoleTable tbRoleTable);

    /**
     * 删除角色管理
     *
     * @param id 角色管理主键
     * @return 结果
     */
    int deleteTbRoleTableById(Long id);

    /**
     * 批量删除角色管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTbRoleTableByIds(Long[] ids);
}
