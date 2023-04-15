package com.ruoyi.chatgpt.mapper;

import com.ruoyi.chatgpt.domain.TbDialogueMain;
import com.ruoyi.chatgpt.domain.TbModelTable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 对话列-主Mapper接口
 *
 * @author ruoyi
 * @date 2023-03-27
 */
public interface TbDialogueMainMapper
{
    /**
     * 查询对话列-主
     *
     * @param id 对话列-主主键
     * @return 对话列-主
     */
    public TbDialogueMain selectTbDialogueMainById(Long id);

    /**
     * 查询对话列-主列表
     *
     * @param tbDialogueMain 对话列-主
     * @return 对话列-主集合
     */
    public List<TbDialogueMain> selectTbDialogueMainList(TbDialogueMain tbDialogueMain);

    /**
     * 新增对话列-主
     *
     * @param tbDialogueMain 对话列-主
     * @return 结果
     */
    public int insertTbDialogueMain(TbDialogueMain tbDialogueMain);

    /**
     * 修改对话列-主
     *
     * @param tbDialogueMain 对话列-主
     * @return 结果
     */
    public int updateTbDialogueMain(TbDialogueMain tbDialogueMain);

    /**
     * 删除对话列-主
     *
     * @param id 对话列-主主键
     * @return 结果
     */
    public int deleteTbDialogueMainById(@Param("id")Long id, @Param("userId")Long userId);

    /**
     * 批量删除对话列-主
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbDialogueMainByIds(Long[] ids);
    public List<TbDialogueMain> selectTbDiglListBydRoleId(Long dRoleId);
}
