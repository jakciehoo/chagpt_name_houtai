package com.ruoyi.chatgpt.mapper;

import com.ruoyi.chatgpt.domain.TbDialogueProcess;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 充值卡-从Mapper接口
 *
 * @author ruoyi
 * @date 2023-03-27
 */
public interface TbDialogueProcessMapper
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
     * 删除充值卡-从
     *
     * @param id 充值卡-从主键
     * @return 结果
     */
    public int deleteTbDialogueProcessById(Long id);

    /**
     * 批量删除充值卡-从
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbDialogueProcessByIds(Long[] ids);


    @Select("select count(user_id) from tb_dialogue_process where user_id = #{userId} ")
    public int getDialogueCount(@Param("userId")Long userId);


    public int deleteTbDialogueProcessBySessionId(@Param("sessionId")Long sessionId,@Param("userId")Long userId);
}
