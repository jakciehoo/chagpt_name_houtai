package com.ruoyi.chatgpt.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.chatgpt.mapper.TbAnsweUserMapper;
import com.ruoyi.chatgpt.domain.TbAnsweUser;
import com.ruoyi.chatgpt.service.ITbAnsweUserService;
import java.util.Objects;
import com.ruoyi.common.utils.SecurityUtils;
import cn.hutool.core.date.DateTime;
/**
 * 小程序用户聊天列Service业务层处理
 *
 * @author ruoyi
 * @date 2023-02-16
 */
@Service
public class TbAnsweUserServiceImpl implements ITbAnsweUserService
{
    @Autowired
    private TbAnsweUserMapper tbAnsweUserMapper;

    /**
     * 查询小程序用户聊天列
     *
     * @param answeUserId 小程序用户聊天列主键
     * @return 小程序用户聊天列
     */
    @Override
    public TbAnsweUser selectTbAnsweUserByAnsweUserId(Long answeUserId)
    {
        return tbAnsweUserMapper.selectTbAnsweUserByAnsweUserId(answeUserId);
    }

    /**
     * 查询小程序用户聊天列列表
     *
     * @param tbAnsweUser 小程序用户聊天列
     * @return 小程序用户聊天列
     */
    @Override
    public List<TbAnsweUser> selectTbAnsweUserList(TbAnsweUser tbAnsweUser)
    {
        return tbAnsweUserMapper.selectTbAnsweUserList(tbAnsweUser);
    }

    /**
     * 新增小程序用户聊天列
     *
     * @param tbAnsweUser 小程序用户聊天列
     * @return 结果
     */
    @Override
    public int insertTbAnsweUser(TbAnsweUser tbAnsweUser)
    {
        tbAnsweUser.setCreateBy("-1");
        tbAnsweUser.setUpdateTime(DateTime.now());
        tbAnsweUser.setUpdateBy("-1");
        tbAnsweUser.setCreateTime(DateUtils.getNowDate());
        return tbAnsweUserMapper.insertTbAnsweUser(tbAnsweUser);
    }

    /**
     * 修改小程序用户聊天列
     *
     * @param tbAnsweUser 小程序用户聊天列
     * @return 结果
     */
    @Override
    public int updateTbAnsweUser(TbAnsweUser tbAnsweUser)
    {

//        TbAnsweUser tbAnsweUser1 = this.selectTbAnsweUserByAnsweUserId(tbAnsweUser.getAnsweUserId());
//        if (tbAnsweUser1.getUserId()!=SecurityUtils.getUserId()){
//            return 0;
//        }
//
//        tbAnsweUser.setUpdateTime(DateUtils.getNowDate());
        return tbAnsweUserMapper.updateTbAnsweUser(tbAnsweUser);
    }

    /**
     * 批量删除小程序用户聊天列
     *
     * @param answeUserIds 需要删除的小程序用户聊天列主键
     * @return 结果
     */
    @Override
    public int deleteTbAnsweUserByAnsweUserIds(Long[] answeUserIds)
    {
//        int count = 0;
//        for (int i=0;i<answeUserIds.length;i++){
//            TbAnsweUser tbAnsweUser = this.selectTbAnsweUserByAnsweUserId(answeUserIds[i]);
//            if (!Objects.isNull(tbAnsweUser)){
//                if (tbAnsweUser.getUserId() != SecurityUtils.getUserId()){
//                    return 0;
//                }
//            }
//        }


        return tbAnsweUserMapper.deleteTbAnsweUserByAnsweUserIds(answeUserIds);
    }

    /**
     * 删除小程序用户聊天列信息
     *
     * @param answeUserId 小程序用户聊天列主键
     * @return 结果
     */
    @Override
    public int deleteTbAnsweUserByAnsweUserId(Long answeUserId)
    {
//    TbAnsweUser tbAnsweUserEqId = this.selectTbAnsweUserByAnsweUserId(answeUserId);
//    if (tbAnsweUserEqId.getUserId()!=SecurityUtils.getUserId()){
//        return 0;
//    }



        return tbAnsweUserMapper.deleteTbAnsweUserByAnsweUserId(answeUserId);
    }























    /**
     * 查询小程序用户聊天列  -----   管理员使用
     *
     * @param answeUserId 小程序用户聊天列主键
     * @return 小程序用户聊天列
     */
    @Override
    public TbAnsweUser selectTbAnsweUserByAnsweUserIdsys(Long answeUserId)
    {
        return tbAnsweUserMapper.selectTbAnsweUserByAnsweUserId(answeUserId);
    }

    /**
     * 查询小程序用户聊天列列表  -----   管理员使用
     *
     * @param tbAnsweUser 小程序用户聊天列
     * @return 小程序用户聊天列
     */
    @Override
    public List<TbAnsweUser> selectTbAnsweUserListsys(TbAnsweUser tbAnsweUser)
    {
        return tbAnsweUserMapper.selectTbAnsweUserList(tbAnsweUser);
    }

    /**
     * 新增小程序用户聊天列  -----   管理员使用
     *
     * @param tbAnsweUser 小程序用户聊天列
     * @return 结果
     */
    @Override
    public int insertTbAnsweUsersys(TbAnsweUser tbAnsweUser)
    {
                tbAnsweUser.setCreateTime(DateUtils.getNowDate());
            return tbAnsweUserMapper.insertTbAnsweUser(tbAnsweUser);
    }

    /**
     * 修改小程序用户聊天列  -----   管理员使用
     *
     * @param tbAnsweUser 小程序用户聊天列
     * @return 结果
     */
    @Override
    public int updateTbAnsweUsersys(TbAnsweUser tbAnsweUser)
    {
                tbAnsweUser.setUpdateTime(DateUtils.getNowDate());
        return tbAnsweUserMapper.updateTbAnsweUser(tbAnsweUser);
    }

    /**
     * 批量删除小程序用户聊天列  -----   管理员使用
     *
     * @param answeUserIds 需要删除的小程序用户聊天列主键
     * @return 结果
     */
    @Override
    public int deleteTbAnsweUserByAnsweUserIdssys(Long[] answeUserIds)
    {
        return tbAnsweUserMapper.deleteTbAnsweUserByAnsweUserIds(answeUserIds);
    }

    /**
     * 删除小程序用户聊天列信息
     *
     * @param answeUserId 小程序用户聊天列主键
     * @return 结果
     */
    @Override
    public int deleteTbAnsweUserByAnsweUserIdsys(Long answeUserId)
    {
        return tbAnsweUserMapper.deleteTbAnsweUserByAnsweUserId(answeUserId);
    }


    @Override
    public TbAnsweUser selectTbAnsweUserByOpenId(String openId){
        return tbAnsweUserMapper.selectTbAnsweUserByOpenId(openId);
    }


}
