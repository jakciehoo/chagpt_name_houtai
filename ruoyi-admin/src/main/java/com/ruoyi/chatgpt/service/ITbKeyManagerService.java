package com.ruoyi.chatgpt.service;

import java.util.List;
import com.ruoyi.chatgpt.domain.TbKeyManager;

/**
 * key管理Service接口
 *
 * @author ruoyi
 * @date 2023-03-06
 */
public interface ITbKeyManagerService
{
    /**
     * 查询key管理
     *
     * @param id key管理主键
     * @return key管理
     */
    public TbKeyManager selectTbKeyManagerById(Long id);

    /**
     * 查询key管理列表
     *
     * @param tbKeyManager key管理
     * @return key管理集合
     */
    public List<TbKeyManager> selectTbKeyManagerList(TbKeyManager tbKeyManager);


    /**
     * 查询key管理列表
     *
     * @param TbKeyManager key管理
     * @return key管理集合
     */
    public List<TbKeyManager> selectTbKeyManagerListCanUse(TbKeyManager tbKeyManager);


    /**
     * 新增key管理
     *
     * @param tbKeyManager key管理
     * @return 结果
     */
    public int insertTbKeyManager(TbKeyManager tbKeyManager);

    /**
     * 修改key管理
     *
     * @param tbKeyManager key管理
     * @return 结果
     */
    public int updateTbKeyManager(TbKeyManager tbKeyManager);

    /**
     * 批量删除key管理
     *
     * @param ids 需要删除的key管理主键集合
     * @return 结果
     */
    public int deleteTbKeyManagerByIds(Long[] ids);

    /**
     * 批量删除key管理
     *
     * @param key 需要删除的key管理主键集合
     * @return 结果
     */
    public void changeKeyStatusToUsed(String key);


    /**
     * 批量删除key管理
     *
     * @param key 需要删除的key管理主键集合
     * @return 结果
     */
    public void changeKeyStatusToDeteleAsk(String key);



    /**
     * 删除key管理信息
     *
     * @param id key管理主键
     * @return 结果
     */
    public int deleteTbKeyManagerById(Long id);









    /**
     * 查询key管理  -----   管理员使用
     *
     * @param id key管理主键
     * @return key管理
     */
    public TbKeyManager selectTbKeyManagerByIdsys(Long id);

    /**
     * 查询key管理列表  -----   管理员使用
     *
     * @param tbKeyManager key管理
     * @return key管理集合
     */
    public List<TbKeyManager> selectTbKeyManagerListsys(TbKeyManager tbKeyManager);

    /**
     * 新增key管理  -----   管理员使用
     *
     * @param tbKeyManager key管理
     * @return 结果
     */
    public int insertTbKeyManagersys(TbKeyManager tbKeyManager);

    /**
     * 修改key管理  -----   管理员使用
     *
     * @param tbKeyManager key管理
     * @return 结果
     */
    public int updateTbKeyManagersys(TbKeyManager tbKeyManager);

    /**
     * 批量删除key管理  -----   管理员使用
     *
     * @param ids 需要删除的key管理主键集合
     * @return 结果
     */
    public int deleteTbKeyManagerByIdssys(Long[] ids);

    /**
     * 删除key管理信息  -----   管理员使用
     *
     * @param id key管理主键
     * @return 结果
     */
    public int deleteTbKeyManagerByIdsys(Long id);
    public String getproxyUrl();
}
