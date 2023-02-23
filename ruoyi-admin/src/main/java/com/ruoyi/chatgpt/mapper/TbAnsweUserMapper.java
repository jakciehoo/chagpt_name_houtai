package com.ruoyi.chatgpt.mapper;

import java.util.List;
import com.ruoyi.chatgpt.domain.TbAnsweUser;

/**
 * 小程序用户聊天列Mapper接口
 *
 * @author ruoyi
 * @date 2023-02-16
 */
public interface TbAnsweUserMapper
{
    /**
     * 查询小程序用户聊天列
     *
     * @param answeUserId 小程序用户聊天列主键
     * @return 小程序用户聊天列
     */
    public TbAnsweUser selectTbAnsweUserByAnsweUserId(Long answeUserId);

    /**
     * 查询小程序用户聊天列列表
     *
     * @param tbAnsweUser 小程序用户聊天列
     * @return 小程序用户聊天列集合
     */
    public List<TbAnsweUser> selectTbAnsweUserList(TbAnsweUser tbAnsweUser);

    /**
     * 新增小程序用户聊天列
     *
     * @param tbAnsweUser 小程序用户聊天列
     * @return 结果
     */
    public int insertTbAnsweUser(TbAnsweUser tbAnsweUser);

    /**
     * 修改小程序用户聊天列
     *
     * @param tbAnsweUser 小程序用户聊天列
     * @return 结果
     */
    public int updateTbAnsweUser(TbAnsweUser tbAnsweUser);

    /**
     * 删除小程序用户聊天列
     *
     * @param answeUserId 小程序用户聊天列主键
     * @return 结果
     */
    public int deleteTbAnsweUserByAnsweUserId(Long answeUserId);

    /**
     * 批量删除小程序用户聊天列
     *
     * @param answeUserIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbAnsweUserByAnsweUserIds(Long[] answeUserIds);


    public TbAnsweUser selectTbAnsweUserByOpenId(String openId);
}
