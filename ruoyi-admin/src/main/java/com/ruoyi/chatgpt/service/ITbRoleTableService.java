package com.ruoyi.chatgpt.service;

import com.ruoyi.chatgpt.domain.TbRoleTable;

import java.util.List;

/**
 * 角色管理Service接口
 *
 * @author ruoyi
 * @date 2023-03-22
 */
public interface ITbRoleTableService
{
    /**
     * 查询角色管理
     *
     * @param id 角色管理主键
     * @return 角色管理
     */
    TbRoleTable selectTbRoleTableById(Long id);

    /**
     * 查询用户选择的角色
     *
     * @param tbRoleTable 角色管理主键
     * @return 角色管理
     */
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
     * 批量删除角色管理
     *
     * @param ids 需要删除的角色管理主键集合
     * @return 结果
     */
    int deleteTbRoleTableByIds(Long[] ids);

    /**
     * 删除角色管理信息
     *
     * @param id 角色管理主键
     * @return 结果
     */
    int deleteTbRoleTableById(Long id);









    /**
     * 查询角色管理  -----   管理员使用
     *
     * @param id 角色管理主键
     * @return 角色管理
     */
    TbRoleTable selectTbRoleTableByIdsys(Long id);

    /**
     * 查询角色管理列表  -----   管理员使用
     *
     * @param tbRoleTable 角色管理
     * @return 角色管理集合
     */
    List<TbRoleTable> selectTbRoleTableListsys(TbRoleTable tbRoleTable);

    /**
     * 新增角色管理  -----   管理员使用
     *
     * @param tbRoleTable 角色管理
     * @return 结果
     */
    int insertTbRoleTablesys(TbRoleTable tbRoleTable);

    /**
     * 修改角色管理  -----   管理员使用
     *
     * @param tbRoleTable 角色管理
     * @return 结果
     */
    int updateTbRoleTablesys(TbRoleTable tbRoleTable);

    /**
     * 批量删除角色管理  -----   管理员使用
     *
     * @param ids 需要删除的角色管理主键集合
     * @return 结果
     */
    int deleteTbRoleTableByIdssys(Long[] ids);

    /**
     * 删除角色管理信息  -----   管理员使用
     *
     * @param id 角色管理主键
     * @return 结果
     */
    int deleteTbRoleTableByIdsys(Long id);
}
