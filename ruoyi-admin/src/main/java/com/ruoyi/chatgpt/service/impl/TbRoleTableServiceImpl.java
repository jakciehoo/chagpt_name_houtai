package com.ruoyi.chatgpt.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.chatgpt.domain.TbRoleTable;
import com.ruoyi.chatgpt.mapper.TbRoleTableMapper;
import com.ruoyi.chatgpt.service.ITbRoleTableService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
/**
 * 角色管理Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-22
 */
@Service
public class TbRoleTableServiceImpl implements ITbRoleTableService
{
    @Autowired
    private TbRoleTableMapper tbRoleTableMapper;

    /**
     * 查询角色管理
     *
     * @param id 角色管理主键
     * @return 角色管理
     */
    @Override
    public TbRoleTable selectTbRoleTableById(Long id)
    {
        return tbRoleTableMapper.selectTbRoleTableById(id);
    }
    @Override
    public TbRoleTable selectTbRoleTableByIdAndShow(TbRoleTable tbRoleTable)
    {
        return tbRoleTableMapper.selectTbRoleTableByIdAndShow(tbRoleTable);
    }



    /**
     * 查询角色管理列表
     *
     * @param tbRoleTable 角色管理
     * @return 角色管理
     */
    @Override
    public List<TbRoleTable> selectTbRoleTableList(TbRoleTable tbRoleTable)
    {
        return tbRoleTableMapper.selectTbRoleTableList(tbRoleTable);
    }

    /**
     * 新增角色管理
     *
     * @param tbRoleTable 角色管理
     * @return 结果
     */
    @Override
    public int insertTbRoleTable(TbRoleTable tbRoleTable)
    {
        Long userId = SecurityUtils.getUserId();
        tbRoleTable.setIsUse(1);
        tbRoleTable.setIsDetele(0);
        tbRoleTable.setUserId(userId);
        tbRoleTable.setCreateBy(userId+"");
        tbRoleTable.setUpdateTime(DateTime.now());
        tbRoleTable.setUpdateBy(userId+"");
        tbRoleTable.setCreateTime(DateUtils.getNowDate());
        return tbRoleTableMapper.insertTbRoleTable(tbRoleTable);
    }

    /**
     * 修改角色管理
     *
     * @param tbRoleTable 角色管理
     * @return 结果
     */
    @Override
    public int updateTbRoleTable(TbRoleTable tbRoleTable)
    {

        TbRoleTable tbRoleTable1 = this.selectTbRoleTableById(tbRoleTable.getId());
        if (tbRoleTable1.getUserId()!=SecurityUtils.getUserId()){
            return 0;
        }

        tbRoleTable.setUpdateTime(DateUtils.getNowDate());
        return tbRoleTableMapper.updateTbRoleTable(tbRoleTable);
    }

    /**
     * 批量删除角色管理
     *
     * @param ids 需要删除的角色管理主键
     * @return 结果
     */
    @Override
    public int deleteTbRoleTableByIds(Long[] ids)
    {
        int count = 0;
        for (int i=0;i<ids.length;i++){
            TbRoleTable tbRoleTable = this.selectTbRoleTableById(ids[i]);
            if (!Objects.isNull(tbRoleTable)){
                if (tbRoleTable.getUserId() != SecurityUtils.getUserId()){
                    return 0;
                }
            }
        }


        return tbRoleTableMapper.deleteTbRoleTableByIds(ids);
    }

    /**
     * 删除角色管理信息
     *
     * @param id 角色管理主键
     * @return 结果
     */
    @Override
    public int deleteTbRoleTableById(Long id)
    {
    TbRoleTable tbRoleTableEqId = this.selectTbRoleTableById(id);
    if (tbRoleTableEqId.getUserId()!=SecurityUtils.getUserId()){
        return 0;
    }



        return tbRoleTableMapper.deleteTbRoleTableById(id);
    }























    /**
     * 查询角色管理  -----   管理员使用
     *
     * @param id 角色管理主键
     * @return 角色管理
     */
    @Override
    public TbRoleTable selectTbRoleTableByIdsys(Long id)
    {
        return tbRoleTableMapper.selectTbRoleTableById(id);
    }

    /**
     * 查询角色管理列表  -----   管理员使用
     *
     * @param tbRoleTable 角色管理
     * @return 角色管理
     */
    @Override
    public List<TbRoleTable> selectTbRoleTableListsys(TbRoleTable tbRoleTable)
    {
        return tbRoleTableMapper.selectTbRoleTableList(tbRoleTable);
    }

    /**
     * 新增角色管理  -----   管理员使用
     *
     * @param tbRoleTable 角色管理
     * @return 结果
     */
    @Override
    public int insertTbRoleTablesys(TbRoleTable tbRoleTable)
    {
            tbRoleTable.setUserId(SecurityUtils.getUserId());
            tbRoleTable.setCreateTime(DateUtils.getNowDate());
            return tbRoleTableMapper.insertTbRoleTable(tbRoleTable);
    }

    /**
     * 修改角色管理  -----   管理员使用
     *
     * @param tbRoleTable 角色管理
     * @return 结果
     */
    @Override
    public int updateTbRoleTablesys(TbRoleTable tbRoleTable)
    {
                tbRoleTable.setUpdateTime(DateUtils.getNowDate());
        return tbRoleTableMapper.updateTbRoleTable(tbRoleTable);
    }

    /**
     * 批量删除角色管理  -----   管理员使用
     *
     * @param ids 需要删除的角色管理主键
     * @return 结果
     */
    @Override
    public int deleteTbRoleTableByIdssys(Long[] ids)
    {
        return tbRoleTableMapper.deleteTbRoleTableByIds(ids);
    }

    /**
     * 删除角色管理信息
     *
     * @param id 角色管理主键
     * @return 结果
     */
    @Override
    public int deleteTbRoleTableByIdsys(Long id)
    {
        return tbRoleTableMapper.deleteTbRoleTableById(id);
    }



}
