package com.ruoyi.chatgpt.service;

import com.ruoyi.chatgpt.domain.TbDialogueMain;
import com.ruoyi.chatgpt.domain.TbModelTable;

import java.util.List;

/**
 * 对话列-主Service接口
 *
 * @author ruoyi
 * @date 2023-03-27
 */
public interface ITbDialogueMainService
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
     * 批量删除对话列-主
     *
     * @param ids 需要删除的对话列-主主键集合
     * @return 结果
     */
    public int deleteTbDialogueMainByIds(Long[] ids);

    /**
     * 删除对话列-主信息
     *
     * @param id 对话列-主主键
     * @return 结果
     */
    public int deleteTbDialogueMainById(Long id,Long userId);









    /**
     * 查询对话列-主  -----   管理员使用
     *
     * @param id 对话列-主主键
     * @return 对话列-主
     */
    public TbDialogueMain selectTbDialogueMainByIdsys(Long id);

    /**
     * 查询对话列-主列表  -----   管理员使用
     *
     * @param tbDialogueMain 对话列-主
     * @return 对话列-主集合
     */
    public List<TbDialogueMain> selectTbDialogueMainListsys(TbDialogueMain tbDialogueMain);

    /**
     * 新增对话列-主  -----   管理员使用
     *
     * @param tbDialogueMain 对话列-主
     * @return 结果
     */
    public int insertTbDialogueMainsys(TbDialogueMain tbDialogueMain);

    /**
     * 修改对话列-主  -----   管理员使用
     *
     * @param tbDialogueMain 对话列-主
     * @return 结果
     */
    public int updateTbDialogueMainsys(TbDialogueMain tbDialogueMain);

    /**
     * 批量删除对话列-主  -----   管理员使用
     *
     * @param ids 需要删除的对话列-主主键集合
     * @return 结果
     */
    public int deleteTbDialogueMainByIdssys(Long[] ids);

    /**
     * 删除对话列-主信息  -----   管理员使用
     *
     * @param id 对话列-主主键
     * @return 结果
     */
    public int deleteTbDialogueMainByIdsys(Long id);
    public List<TbDialogueMain> creatDigFlag(TbDialogueMain tbDialogueMain);

    public TbDialogueMain creatNewDig(TbModelTable tbModelTable);
//    public TbDialogueMain continueNewDig(TbDialogueMain tbDialogueMain);

    public int deteleDig(Long id);
    /**
     * 通过模型ID查询list
     *
     * @param dRoleId 模型建设
     * @return 模型建设集合
     */
    public List<TbDialogueMain> selectTbDiglListBydRoleId(Long dRoleId);
}
