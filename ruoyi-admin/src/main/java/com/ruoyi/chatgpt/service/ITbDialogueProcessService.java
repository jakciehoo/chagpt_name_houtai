package com.ruoyi.chatgpt.service;

import com.ruoyi.chatgpt.domain.TbDialogueProcess;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 充值卡-从Service接口
 *
 * @author ruoyi
 * @date 2023-03-27
 */
public interface ITbDialogueProcessService
{
    /**
     * 查询充值卡-从
     *
     * @param id 充值卡-从主键
     * @return 充值卡-从
     */
    public TbDialogueProcess selectTbDialogueProcessById(Long id);

    /**
     * 查询充值卡-从列表
     *
     * @param tbDialogueProcess 充值卡-从
     * @return 充值卡-从集合
     */
    public List<TbDialogueProcess> selectTbDialogueProcessList(TbDialogueProcess tbDialogueProcess);

    /**
     * 查询对话详情列表
     *
     * @param tbDialogueProcess 对话详情
     * @return 充值卡-从集合
     */
    public List<TbDialogueProcess> selectTbDialogueProcessListByLimitDesc(TbDialogueProcess tbDialogueProcess);



    /**
     * 新增充值卡-从
     *
     * @param tbDialogueProcess 充值卡-从
     * @return 结果
     */
    public int insertTbDialogueProcess(TbDialogueProcess tbDialogueProcess);

    /**
     * 修改充值卡-从
     *
     * @param tbDialogueProcess 充值卡-从
     * @return 结果
     */
    public int updateTbDialogueProcess(TbDialogueProcess tbDialogueProcess);

    /**
     * 批量删除充值卡-从
     *
     * @param ids 需要删除的充值卡-从主键集合
     * @return 结果
     */
    public int deleteTbDialogueProcessByIds(Long[] ids);

    /**
     * 删除充值卡-从信息
     *
     * @param id 充值卡-从主键
     * @return 结果
     */
    public int deleteTbDialogueProcessById(Long id);









    /**
     * 查询充值卡-从  -----   管理员使用
     *
     * @param id 充值卡-从主键
     * @return 充值卡-从
     */
    public TbDialogueProcess selectTbDialogueProcessByIdsys(Long id);

    /**
     * 查询充值卡-从列表  -----   管理员使用
     *
     * @param tbDialogueProcess 充值卡-从
     * @return 充值卡-从集合
     */
    public List<TbDialogueProcess> selectTbDialogueProcessListsys(TbDialogueProcess tbDialogueProcess);

    /**
     * 新增充值卡-从  -----   管理员使用
     *
     * @param tbDialogueProcess 充值卡-从
     * @return 结果
     */
    public int insertTbDialogueProcesssys(TbDialogueProcess tbDialogueProcess);

    /**
     * 修改充值卡-从  -----   管理员使用
     *
     * @param tbDialogueProcess 充值卡-从
     * @return 结果
     */
    public int updateTbDialogueProcesssys(TbDialogueProcess tbDialogueProcess);

    /**
     * 批量删除充值卡-从  -----   管理员使用
     *
     * @param ids 需要删除的充值卡-从主键集合
     * @return 结果
     */
    public int deleteTbDialogueProcessByIdssys(Long[] ids);

    /**
     * 删除充值卡-从信息  -----   管理员使用
     *
     * @param id 充值卡-从主键
     * @return 结果
     */
    public int deleteTbDialogueProcessByIdsys(Long id);



    /**
     *
     * @return 结果
     */
    public int getDialogueCount();



    /**
     * 查询充值卡-从列表  -----   管理员使用
     *
     * @param tbDialogueProcess 充值卡-从
     * @return 充值卡-从集合
     */
    public List<TbDialogueProcess> listDialogue(TbDialogueProcess tbDialogueProcess);
    public int deleteTbDialogueProcessBySessionId(Long sessionId,Long userId);
}
