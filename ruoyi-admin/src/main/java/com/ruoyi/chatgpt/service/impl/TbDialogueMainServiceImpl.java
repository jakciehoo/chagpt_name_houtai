package com.ruoyi.chatgpt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.ai.doamin.SettingVO;
import com.ruoyi.chatgpt.domain.TbDialogueMain;
import com.ruoyi.chatgpt.domain.TbDialogueProcess;
import com.ruoyi.chatgpt.domain.TbModelTable;
import com.ruoyi.chatgpt.mapper.TbDialogueMainMapper;
import com.ruoyi.chatgpt.service.ITbDialogueMainService;
import com.ruoyi.chatgpt.service.ITbDialogueProcessService;
import com.ruoyi.chatgpt.service.ITbModelTableService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 对话列-主Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-27
 */
@Service
public class TbDialogueMainServiceImpl implements ITbDialogueMainService {
    @Autowired
    private TbDialogueMainMapper tbDialogueMainMapper;

    @Autowired
    private ITbDialogueProcessService tbDialogueProcessService;

    @Autowired
    private ITbModelTableService tbModelTableService;
    /**
     * 查询对话列-主
     *
     * @param id 对话列-主主键
     * @return 对话列-主
     */
    @Override
    public TbDialogueMain selectTbDialogueMainById(Long id) {
        return tbDialogueMainMapper.selectTbDialogueMainById(id);
    }

    /**
     * 查询对话列-主列表
     *
     * @param tbDialogueMain 对话列-主
     * @return 对话列-主
     */
    @Override
    public List<TbDialogueMain> selectTbDialogueMainList(TbDialogueMain tbDialogueMain) {
        return tbDialogueMainMapper.selectTbDialogueMainList(tbDialogueMain);
    }

    /**
     * 新增对话列-主
     *
     * @param tbDialogueMain 对话列-主
     * @return 结果
     */
    @Override
    public int insertTbDialogueMain(TbDialogueMain tbDialogueMain) {
//        //如果ID不为空
//        TbDialogueMain tbDialogueMainUse = new TbDialogueMain();
////            DateTime nowTime = DateTime.now();
////            String yyyyMMddHHmmss = DateUtil.format(nowTime, "yyyyMMddHHmmss");
//        if (StrUtil.isBlank(tbDialogueMain.getDialogueName())){
//            tbDialogueMainUse.setDialogueName("NewChat");
//        }
//        if (!Objects.isNull(tbDialogueMain.getDialogueRoleId())){
//            tbDialogueMainUse.setDialogueRoleId(tbDialogueMain.getDialogueRoleId());
//        }
////      tbDialogueMainUse.setSessionId(tbDialogueMain.getSessionId());
//        Long userId = SecurityUtils.getUserId();
//        tbDialogueMainUse.setUserId(userId);
//        tbDialogueMainUse.setCreateBy(userId + "");
//        tbDialogueMainUse.setUpdateTime(DateTime.now());
//        tbDialogueMainUse.setUpdateBy(userId + "");
//        tbDialogueMainUse.setCreateTime(DateUtils.getNowDate());
        return tbDialogueMainMapper.insertTbDialogueMain(tbDialogueMain);
    }

    /**
     * 修改对话列-主
     *
     * @param tbDialogueMain 对话列-主
     * @return 结果
     */
    @Override
    public int updateTbDialogueMain(TbDialogueMain tbDialogueMain) {
        if (Objects.isNull(tbDialogueMain.getId())){
            throw new RuntimeException("会话不存在");
        }
        if (Objects.isNull(tbDialogueMain.getDialogueName())){
            throw new RuntimeException("会话名称不可为空");
        }
        //如果ID不为空
        TbDialogueMain tbDialogueMainUse = new TbDialogueMain();
        if (!Objects.isNull(tbDialogueMain.getId())){
            tbDialogueMainUse =  this.tbMainIsExtise(tbDialogueMain);
        }
        if (tbDialogueMainUse.getUserId() != SecurityUtils.getUserId()) {
            return 0;
        }
        tbDialogueMainUse.setUpdateTime(DateUtils.getNowDate());
        return tbDialogueMainMapper.updateTbDialogueMain(tbDialogueMainUse);
    }

    /**
     * 批量删除对话列-主
     *
     * @param ids 需要删除的对话列-主主键
     * @return 结果
     */
    @Override
    public int deleteTbDialogueMainByIds(Long[] ids) {
        int count = 0;
        for (int i = 0; i < ids.length; i++) {
            TbDialogueMain tbDialogueMain = this.selectTbDialogueMainById(ids[i]);
            if (!Objects.isNull(tbDialogueMain)) {
                if (tbDialogueMain.getUserId() != SecurityUtils.getUserId()) {
                    return 0;
                }
            }
        }


        return tbDialogueMainMapper.deleteTbDialogueMainByIds(ids);
    }

    /**
     * 删除对话列-主信息
     *
     * @param id 对话列-主主键
     * @return 结果
     */
    @Override
    public int deleteTbDialogueMainById(Long id,Long userId) {
        return tbDialogueMainMapper.deleteTbDialogueMainById(id,userId);
    }


    /**
     * 查询对话列-主  -----   管理员使用
     *
     * @param id 对话列-主主键
     * @return 对话列-主
     */
    @Override
    public TbDialogueMain selectTbDialogueMainByIdsys(Long id) {
        return tbDialogueMainMapper.selectTbDialogueMainById(id);
    }

    /**
     * 查询对话列-主列表  -----   管理员使用
     *
     * @param tbDialogueMain 对话列-主
     * @return 对话列-主
     */
    @Override
    public List<TbDialogueMain> selectTbDialogueMainListsys(TbDialogueMain tbDialogueMain) {
        return tbDialogueMainMapper.selectTbDialogueMainList(tbDialogueMain);
    }

    /**
     * 新增对话列-主  -----   管理员使用
     *
     * @param tbDialogueMain 对话列-主
     * @return 结果
     */
    @Override
    public int insertTbDialogueMainsys(TbDialogueMain tbDialogueMain) {
        tbDialogueMain.setCreateTime(DateUtils.getNowDate());
        return tbDialogueMainMapper.insertTbDialogueMain(tbDialogueMain);
    }

    /**
     * 修改对话列-主  -----   管理员使用
     *
     * @param tbDialogueMain 对话列-主
     * @return 结果
     */
    @Override
    public int updateTbDialogueMainsys(TbDialogueMain tbDialogueMain) {
        tbDialogueMain.setUpdateTime(DateUtils.getNowDate());
        return tbDialogueMainMapper.updateTbDialogueMain(tbDialogueMain);
    }

    /**
     * 批量删除对话列-主  -----   管理员使用
     *
     * @param ids 需要删除的对话列-主主键
     * @return 结果
     */
    @Override
    public int deleteTbDialogueMainByIdssys(Long[] ids) {
        return tbDialogueMainMapper.deleteTbDialogueMainByIds(ids);
    }

    /**
     * 删除对话列-主信息
     *
     * @param id 对话列-主主键
     * @return 结果
     */
    @Override
    public int deleteTbDialogueMainByIdsys(Long id) {
        return tbDialogueMainMapper.deleteTbDialogueMainById(id,SecurityUtils.getUserId());
    }
    @Override
    public List<TbDialogueMain> creatDigFlag(TbDialogueMain tbDialogueMain) {
        tbDialogueMain.setUserId(SecurityUtils.getUserId());
        List<TbDialogueMain> tbDialogueMains =  this.selectTbDialogueMainList(tbDialogueMain);
        //查询对话数量
        TbDialogueProcess tbDialogueProcess = new TbDialogueProcess();
        tbDialogueProcess.setUserId(SecurityUtils.getUserId());
        List<TbDialogueProcess> tbDialogueProcesses = tbDialogueProcessService.selectTbDialogueProcessList(tbDialogueProcess);
        Map<Long, Long> countBySessionId = null;
        if (CollectionUtil.isNotEmpty(tbDialogueProcesses)){
            countBySessionId = tbDialogueProcesses.stream()
                    .collect(Collectors.groupingBy(TbDialogueProcess::getSessionId, Collectors.counting()));
        }

        //查询标签,根据对话roleID->查询到模型信息
        TbModelTable tbModelTable = new TbModelTable();
        List<TbModelTable> tbModelTables = tbModelTableService.selectTbModelTableList(tbModelTable);
        Map<Long, String>  collectById = null;
        if (CollectionUtil.isNotEmpty(tbModelTables)){
            collectById = tbModelTables.stream().collect(Collectors.toMap(TbModelTable::getId, TbModelTable::getModelName));
        }
        if (CollectionUtil.isNotEmpty(tbDialogueMains)){
            for (TbDialogueMain main:tbDialogueMains){
                //处理每条对话次数
                if (Objects.isNull(countBySessionId) || Objects.isNull(countBySessionId.get(main.getId()))){
                    main.setDigNum(0L);
                }else {
                    main.setDigNum(countBySessionId.get(main.getId()));
                }
                //处理标签
                if (Objects.isNull(collectById) || Objects.isNull(collectById.get(main.getDialogueRoleId()))){
                    main.setModelName("未选择对话主题");
                }else {
                    main.setModelName(collectById.get(main.getDialogueRoleId()));
                }
            }
            return tbDialogueMains;
        }
        return new ArrayList<>();
    }

    @Override
    public TbDialogueMain creatNewDig(TbModelTable tbModelTable){
        if (Objects.isNull(tbModelTable.getId())){
            throw  new RuntimeException("对话模型为空");
        }
        if (StrUtil.isBlank(tbModelTable.getModelName())){
                throw  new RuntimeException("对话主题为空");
        }
        //新建对话列表
        TbDialogueMain tbDialogueMain = new TbDialogueMain();
        //添加模型ID
        tbDialogueMain.setDialogueRoleId(tbModelTable.getId());
        tbDialogueMain.setDialogueName(tbModelTable.getModelName());
        tbDialogueMain.setUserId(SecurityUtils.getUserId());
        tbDialogueMain.setIsDetele(0);
        tbDialogueMain.setCreateBy(SecurityUtils.getUserId()+"");
        tbDialogueMain.setCreateTime(DateTime.now());
        tbDialogueMain.setUpdateBy(SecurityUtils.getUserId()+"");
        tbDialogueMain.setUpdateTime(DateTime.now());
        int i = this.insertTbDialogueMain(tbDialogueMain);
        if (i==1){
            return tbDialogueMain;
        }else {
            throw new RuntimeException("新建会话失败");
        }

    }


    @Override
    @Transactional
    public int deteleDig(Long id) {
        int i = this.deleteTbDialogueMainById(id,SecurityUtils.getUserId());
        if (i<1){
            throw new RuntimeException("删除对话失败");
        }
         tbDialogueProcessService.deleteTbDialogueProcessBySessionId(id,SecurityUtils.getUserId());
        return 1;
    }
//    @Override
//    public TbDialogueMain continueNewDig(TbDialogueMain tbDialogueMain){
//        Long id = tbDialogueMain.getId();
//        TbDialogueMain tbDialogueMainOne = this.selectTbDialogueMainById(id);
//        if (Objects.isNull(tbDialogueMainOne)){
//            throw new RuntimeException("会话获取失败");
//        }
//        return
//    }
    @Override
    public  List<TbDialogueMain>  selectTbDiglListBydRoleId(Long dRoleId)
    {
        return tbDialogueMainMapper.selectTbDiglListBydRoleId(dRoleId);
    }
    /**
     * 查询的对话列表是否存在
     *
     * @param tbDialogueMain
     */
    public TbDialogueMain tbMainIsExtise(TbDialogueMain tbDialogueMain) {
        //查询是否有对话框列表
        TbDialogueMain tbDialogueMainUse = this.selectTbDialogueMainById(tbDialogueMain.getId());
        if (Objects.isNull(tbDialogueMainUse)) {
            throw new RuntimeException("对话内容不存在");
        }
        return tbDialogueMainUse;
    }

}
