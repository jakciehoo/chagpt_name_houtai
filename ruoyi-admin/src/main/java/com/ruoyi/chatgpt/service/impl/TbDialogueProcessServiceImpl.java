package com.ruoyi.chatgpt.service.impl;

import cn.hutool.core.date.DateTime;
import com.ruoyi.chatgpt.domain.TbDialogueProcess;
import com.ruoyi.chatgpt.mapper.TbDialogueProcessMapper;
import com.ruoyi.chatgpt.service.ITbDialogueProcessService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 充值卡-从Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-27
 */
@Service
public class TbDialogueProcessServiceImpl implements ITbDialogueProcessService
{
    @Autowired
    private TbDialogueProcessMapper tbDialogueProcessMapper;

    /**
     * 查询充值卡-从
     *
     * @param id 充值卡-从主键
     * @return 充值卡-从
     */
    @Override
    public TbDialogueProcess selectTbDialogueProcessById(Long id)
    {
        return tbDialogueProcessMapper.selectTbDialogueProcessById(id);
    }

    /**
     * 查询充值卡-从列表
     *
     * @param tbDialogueProcess 充值卡-从
     * @return 充值卡-从
     */
    @Override
    public List<TbDialogueProcess> selectTbDialogueProcessList(TbDialogueProcess tbDialogueProcess)
    {
        return tbDialogueProcessMapper.selectTbDialogueProcessList(tbDialogueProcess);
    }


    @Override
    public List<TbDialogueProcess> selectTbDialogueProcessListByLimitDesc(TbDialogueProcess tbDialogueProcess)
    {
        return tbDialogueProcessMapper.selectTbDialogueProcessListByLimitDesc(tbDialogueProcess);
    }

    /**
     * 新增充值卡-从
     *
     * @param tbDialogueProcess 充值卡-从
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTbDialogueProcess(TbDialogueProcess tbDialogueProcess)
    {
        Long userId = SecurityUtils.getUserId();
        tbDialogueProcess.setUserId(userId);
        tbDialogueProcess.setCreateBy(userId+"");
        tbDialogueProcess.setUpdateTime(DateTime.now());
        tbDialogueProcess.setUpdateBy(userId+"");
        tbDialogueProcess.setCreateTime(DateUtils.getNowDate());
        tbDialogueProcess.setIsDetele(0);
        return tbDialogueProcessMapper.insertTbDialogueProcess(tbDialogueProcess);
    }

    /**
     * 修改充值卡-从
     *
     * @param tbDialogueProcess 充值卡-从
     * @return 结果
     */
    @Override
    public int updateTbDialogueProcess(TbDialogueProcess tbDialogueProcess)
    {

        TbDialogueProcess tbDialogueProcess1 = this.selectTbDialogueProcessById(tbDialogueProcess.getId());
        if (tbDialogueProcess1.getUserId()!=SecurityUtils.getUserId()){
            return 0;
        }

        tbDialogueProcess.setUpdateTime(DateUtils.getNowDate());
        return tbDialogueProcessMapper.updateTbDialogueProcess(tbDialogueProcess);
    }

    /**
     * 批量删除充值卡-从
     *
     * @param ids 需要删除的充值卡-从主键
     * @return 结果
     */
    @Override
    public int deleteTbDialogueProcessByIds(Long[] ids)
    {
        int count = 0;
        for (int i=0;i<ids.length;i++){
            TbDialogueProcess tbDialogueProcess = this.selectTbDialogueProcessById(ids[i]);
            if (!Objects.isNull(tbDialogueProcess)){
                if (tbDialogueProcess.getUserId() != SecurityUtils.getUserId()){
                    return 0;
                }
            }
        }


        return tbDialogueProcessMapper.deleteTbDialogueProcessByIds(ids);
    }

    /**
     * 删除充值卡-从信息
     *
     * @param id 充值卡-从主键
     * @return 结果
     */
    @Override
    public int deleteTbDialogueProcessById(Long id)
    {
    TbDialogueProcess tbDialogueProcessEqId = this.selectTbDialogueProcessById(id);
    if (tbDialogueProcessEqId.getUserId()!=SecurityUtils.getUserId()){
        return 0;
    }
        return tbDialogueProcessMapper.deleteTbDialogueProcessById(id);
    }


    @Override
    public int deleteTbDialogueProcessBySessionId(Long sessionId,Long userId)
    {

        return tbDialogueProcessMapper.deleteTbDialogueProcessBySessionId(sessionId,userId);
    }























    /**
     * 查询充值卡-从  -----   管理员使用
     *
     * @param id 充值卡-从主键
     * @return 充值卡-从
     */
    @Override
    public TbDialogueProcess selectTbDialogueProcessByIdsys(Long id)
    {
        return tbDialogueProcessMapper.selectTbDialogueProcessById(id);
    }

    /**
     * 查询充值卡-从列表  -----   管理员使用
     *
     * @param tbDialogueProcess 充值卡-从
     * @return 充值卡-从
     */
    @Override
    public List<TbDialogueProcess> selectTbDialogueProcessListsys(TbDialogueProcess tbDialogueProcess)
    {
        return tbDialogueProcessMapper.selectTbDialogueProcessList(tbDialogueProcess);
    }

    /**
     * 新增充值卡-从  -----   管理员使用
     *
     * @param tbDialogueProcess 充值卡-从
     * @return 结果
     */
    @Override
    public int insertTbDialogueProcesssys(TbDialogueProcess tbDialogueProcess)
    {
                tbDialogueProcess.setCreateTime(DateUtils.getNowDate());
            return tbDialogueProcessMapper.insertTbDialogueProcess(tbDialogueProcess);
    }

    /**
     * 修改充值卡-从  -----   管理员使用
     *
     * @param tbDialogueProcess 充值卡-从
     * @return 结果
     */
    @Override
    public int updateTbDialogueProcesssys(TbDialogueProcess tbDialogueProcess)
    {
                tbDialogueProcess.setUpdateTime(DateUtils.getNowDate());
        return tbDialogueProcessMapper.updateTbDialogueProcess(tbDialogueProcess);
    }

    /**
     * 批量删除充值卡-从  -----   管理员使用
     *
     * @param ids 需要删除的充值卡-从主键
     * @return 结果
     */
    @Override
    public int deleteTbDialogueProcessByIdssys(Long[] ids)
    {
        return tbDialogueProcessMapper.deleteTbDialogueProcessByIds(ids);
    }

    /**
     * 删除充值卡-从信息
     *
     * @param id 充值卡-从主键
     * @return 结果
     */
    @Override
    public int deleteTbDialogueProcessByIdsys(Long id)
    {
        return tbDialogueProcessMapper.deleteTbDialogueProcessById(id);
    }

    @Override
    public int getDialogueCount() {
        return tbDialogueProcessMapper.getDialogueCount(SecurityUtils.getUserId());
    }

    @Override
    public List<TbDialogueProcess> listDialogue(TbDialogueProcess tbDialogueProcess) {

        List<TbDialogueProcess> tbDialogueProcesses = tbDialogueProcessMapper.selectTbDialogueProcessList(tbDialogueProcess);
        return tbDialogueProcesses.stream().sorted(Comparator.comparing(TbDialogueProcess::getCreateTime)).collect(Collectors.toList());
    }


}
