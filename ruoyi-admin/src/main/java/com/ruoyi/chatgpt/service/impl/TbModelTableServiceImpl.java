package com.ruoyi.chatgpt.service.impl;

import java.util.List;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.chatgpt.mapper.TbModelTableMapper;
import com.ruoyi.chatgpt.domain.TbModelTable;
import com.ruoyi.chatgpt.service.ITbModelTableService;
import java.util.Objects;
import com.ruoyi.common.utils.SecurityUtils;
import cn.hutool.core.date.DateTime;
/**
 * 模型建设Service业务层处理
 *
 * @author zhx
 * @date 2023-04-11
 */
@Service
public class TbModelTableServiceImpl implements ITbModelTableService
{
    @Autowired
    private TbModelTableMapper tbModelTableMapper;

    /**
     * 查询模型建设
     *
     * @param id 模型建设主键
     * @return 模型建设
     */
    @Override
    public TbModelTable selectTbModelTableById(Long id)
    {
        return tbModelTableMapper.selectTbModelTableById(id);
    }

    /**
     * 查询模型建设列表
     *
     * @param tbModelTable 模型建设
     * @return 模型建设
     */
    @Override
    public List<TbModelTable> selectTbModelTableList(TbModelTable tbModelTable)
    {
        return tbModelTableMapper.selectTbModelTableList(tbModelTable);
    }

    /**
     * 新增模型建设
     *
     * @param tbModelTable 模型建设
     * @return 结果
     */
    @Override
    public int insertTbModelTable(TbModelTable tbModelTable)
    {
        Long userId = SecurityUtils.getUserId();
        tbModelTable.setCreateBy(userId+"");
        tbModelTable.setUpdateTime(DateTime.now());
        tbModelTable.setUpdateBy(userId+"");
        tbModelTable.setCreateTime(DateUtils.getNowDate());
        return tbModelTableMapper.insertTbModelTable(tbModelTable);
    }

    /**
     * 修改模型建设
     *
     * @param tbModelTable 模型建设
     * @return 结果
     */
    @Override
    public int updateTbModelTable(TbModelTable tbModelTable)
    {

        TbModelTable tbModelTable1 = this.selectTbModelTableById(tbModelTable.getId());
//        if (tbModelTable1.getUserId()!=SecurityUtils.getUserId()){
//            return 0;
//        }

        tbModelTable.setUpdateTime(DateUtils.getNowDate());
        return tbModelTableMapper.updateTbModelTable(tbModelTable);
    }

    /**
     * 批量删除模型建设
     *
     * @param ids 需要删除的模型建设主键
     * @return 结果
     */
    @Override
    public int deleteTbModelTableByIds(Long[] ids)
    {
//        int count = 0;
//        for (int i=0;i<ids.length;i++){
//            TbModelTable tbModelTable = this.selectTbModelTableById(ids[i]);
//            if (!Objects.isNull(tbModelTable)){
//                if (tbModelTable.getUserId() != SecurityUtils.getUserId()){
//                    return 0;
//                }
//            }
//        }


        return tbModelTableMapper.deleteTbModelTableByIds(ids);
    }

    /**
     * 删除模型建设信息
     *
     * @param id 模型建设主键
     * @return 结果
     */
    @Override
    public int deleteTbModelTableById(Long id)
    {
    TbModelTable tbModelTableEqId = this.selectTbModelTableById(id);
//    if (tbModelTableEqId.getUserId()!=SecurityUtils.getUserId()){
//        return 0;
//    }



        return tbModelTableMapper.deleteTbModelTableById(id);
    }























    /**
     * 查询模型建设  -----   管理员使用
     *
     * @param id 模型建设主键
     * @return 模型建设
     */
    @Override
    public TbModelTable selectTbModelTableByIdsys(Long id)
    {
        return tbModelTableMapper.selectTbModelTableById(id);
    }

    /**
     * 查询模型建设列表  -----   管理员使用
     *
     * @param tbModelTable 模型建设
     * @return 模型建设
     */
    @Override
    public List<TbModelTable> selectTbModelTableListsys(TbModelTable tbModelTable)
    {
        return tbModelTableMapper.selectTbModelTableList(tbModelTable);
    }

    /**
     * 新增模型建设  -----   管理员使用
     *
     * @param tbModelTable 模型建设
     * @return 结果
     */
    @Override
    public int insertTbModelTablesys(TbModelTable tbModelTable)
    {

            tbModelTable.setCreateTime(DateUtils.getNowDate());
            return tbModelTableMapper.insertTbModelTable(tbModelTable);
    }

    /**
     * 修改模型建设  -----   管理员使用
     *
     * @param tbModelTable 模型建设
     * @return 结果
     */
    @Override
    public int updateTbModelTablesys(TbModelTable tbModelTable)
    {
                tbModelTable.setUpdateTime(DateUtils.getNowDate());
        return tbModelTableMapper.updateTbModelTable(tbModelTable);
    }

    /**
     * 批量删除模型建设  -----   管理员使用
     *
     * @param ids 需要删除的模型建设主键
     * @return 结果
     */
    @Override
    public int deleteTbModelTableByIdssys(Long[] ids)
    {
        return tbModelTableMapper.deleteTbModelTableByIds(ids);
    }

    /**
     * 删除模型建设信息
     *
     * @param id 模型建设主键
     * @return 结果
     */
    @Override
    public int deleteTbModelTableByIdsys(Long id)
    {
        return tbModelTableMapper.deleteTbModelTableById(id);
    }


    @Override
    public  List<TbModelTable>  selectTbModelListBydRoleId(Long dRoleId)
    {
        return tbModelTableMapper.selectTbModelListBydRoleId(dRoleId);
    }

}
