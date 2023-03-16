package com.ruoyi.chatgpt.service.impl;

import java.util.*;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.impl.CollectionConverter;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.ruoyi.ai.Gpt35TurboVO;
import com.ruoyi.chatgpt.domain.TbAnsweEmploy;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.json.JsonUtil;
import com.ruoyi.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.ruoyi.chatgpt.mapper.TbKeyManagerMapper;
import com.ruoyi.chatgpt.domain.TbKeyManager;
import com.ruoyi.chatgpt.service.ITbKeyManagerService;
import com.ruoyi.common.utils.SecurityUtils;
import cn.hutool.core.date.DateTime;

import static com.ruoyi.common.core.domain.AjaxResult.error;
import static com.ruoyi.common.core.domain.AjaxResult.success;

/**
 * key管理Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-06
 */
@Service
public class TbKeyManagerServiceImpl implements ITbKeyManagerService
{
    @Autowired
    private TbKeyManagerMapper tbKeyManagerMapper;
    @Autowired
    private ISysConfigService iSysConfigService;
    /**
     * 查询key管理
     *
     * @param id key管理主键
     * @return key管理
     */
    @Override
    public TbKeyManager selectTbKeyManagerById(Long id)
    {
        return tbKeyManagerMapper.selectTbKeyManagerById(id);
    }

    /**
     * 查询key管理列表
     *
     * @param tbKeyManager key管理
     * @return key管理
     */
    @Override
    public List<TbKeyManager> selectTbKeyManagerList(TbKeyManager tbKeyManager)
    {
        return tbKeyManagerMapper.selectTbKeyManagerList(tbKeyManager);
    }

    /**
     * 查询key管理列表
     *
     * @param tbKeyManager key管理
     * @return key管理
     */
    @Override
    public List<TbKeyManager> selectTbKeyManagerListCanUse(TbKeyManager tbKeyManager)
    {
        tbKeyManager.setIsUse(1);
        return tbKeyManagerMapper.selectTbKeyManagerList(tbKeyManager);
    }


    /**
     * 新增key管理
     *
     * @param tbKeyManager key管理
     * @return 结果
     */
    @Override
    public int insertTbKeyManager(TbKeyManager tbKeyManager)
    {

        Long userId = SecurityUtils.getUserId();
        tbKeyManager.setUserId(userId);
        tbKeyManager.setCreateBy(userId+"");
        tbKeyManager.setUpdateTime(DateTime.now());
        tbKeyManager.setUpdateBy(userId+"");
        tbKeyManager.setCreateTime(DateUtils.getNowDate());
        tbKeyManager.setIsUse(1);
        tbKeyManager.setIsDetele(1);
        return tbKeyManagerMapper.insertTbKeyManager(tbKeyManager);
    }

    /**
     * 修改key管理
     *
     * @param tbKeyManager key管理
     * @return 结果
     */
    @Override
    public int updateTbKeyManager(TbKeyManager tbKeyManager)
    {

        TbKeyManager tbKeyManager1 = this.selectTbKeyManagerById(tbKeyManager.getId());
        if (tbKeyManager1.getUserId()!=SecurityUtils.getUserId()){
            return 0;
        }

        tbKeyManager.setUpdateTime(DateUtils.getNowDate());
        return tbKeyManagerMapper.updateTbKeyManager(tbKeyManager);
    }

    /**
     * 批量删除key管理
     *
     * @param ids 需要删除的key管理主键
     * @return 结果
     */
    @Override
    public int deleteTbKeyManagerByIds(Long[] ids)
    {
        int count = 0;
        for (int i=0;i<ids.length;i++){
            TbKeyManager tbKeyManager = this.selectTbKeyManagerById(ids[i]);
            if (!Objects.isNull(tbKeyManager)){
                if (tbKeyManager.getUserId() != SecurityUtils.getUserId()){
                    return 0;
                }
            }
        }


        return tbKeyManagerMapper.deleteTbKeyManagerByIds(ids);
    }



    /**
     * 批量删除key管理
     *
     * @param key 需要删除的key管理主键
     * @return 结果
     */
    @Override
    public void changeKeyStatusToUsed(String key)
    {
         tbKeyManagerMapper.changeKeyStatusToUsed(key);
    }

    /**
     * 批量删除key管理
     *
     * @param key 需要删除的key管理主键
     * @return 结果
     */
    @Override
    public void changeKeyStatusToDeteleAsk(String key)
    {
        tbKeyManagerMapper.changeKeyStatusToDeteleAsk(key);
    }



    /**
     * 删除key管理信息
     *
     * @param id key管理主键
     * @return 结果
     */
    @Override
    public int deleteTbKeyManagerById(Long id)
    {
    TbKeyManager tbKeyManagerEqId = this.selectTbKeyManagerById(id);
    if (tbKeyManagerEqId.getUserId()!=SecurityUtils.getUserId()){
        return 0;
    }



        return tbKeyManagerMapper.deleteTbKeyManagerById(id);
    }























    /**
     * 查询key管理  -----   管理员使用
     *
     * @param id key管理主键
     * @return key管理
     */
    @Override
    public TbKeyManager selectTbKeyManagerByIdsys(Long id)
    {
        return tbKeyManagerMapper.selectTbKeyManagerById(id);
    }

    /**
     * 查询key管理列表  -----   管理员使用
     *
     * @param tbKeyManager key管理
     * @return key管理
     */
    @Override
    public List<TbKeyManager> selectTbKeyManagerListsys(TbKeyManager tbKeyManager)
    {
        return tbKeyManagerMapper.selectTbKeyManagerList(tbKeyManager);
    }

    /**
     * 新增key管理  -----   管理员使用
     *
     * @param tbKeyManager key管理
     * @return 结果
     */
    @Override
    public int insertTbKeyManagersys(TbKeyManager tbKeyManager)
    {
                tbKeyManager.setCreateTime(DateUtils.getNowDate());
            return tbKeyManagerMapper.insertTbKeyManager(tbKeyManager);
    }

    /**
     * 修改key管理  -----   管理员使用
     *
     * @param tbKeyManager key管理
     * @return 结果
     */
    @Override
    public int updateTbKeyManagersys(TbKeyManager tbKeyManager)
    {
                tbKeyManager.setUpdateTime(DateUtils.getNowDate());
        return tbKeyManagerMapper.updateTbKeyManager(tbKeyManager);
    }

    /**
     * 批量删除key管理  -----   管理员使用
     *
     * @param ids 需要删除的key管理主键
     * @return 结果
     */
    @Override
    public int deleteTbKeyManagerByIdssys(Long[] ids)
    {
        return tbKeyManagerMapper.deleteTbKeyManagerByIds(ids);
    }

    /**
     * 删除key管理信息
     *
     * @param id key管理主键
     * @return 结果
     */
    @Override
    public int deleteTbKeyManagerByIdsys(Long id)
    {
        return tbKeyManagerMapper.deleteTbKeyManagerById(id);
    }




    @Scheduled(cron ="0 0/30 * * * ? ")
    public void checkKey()
    {
        System.out.println("30分鐘執行一次");
        TbKeyManager tbKeyManager = new TbKeyManager();
        tbKeyManager.setIsUse(0);//已经耗尽
        tbKeyManager.setIsDetele(1);//但是未删除
        //可用的key
        List<TbKeyManager> tbKeyManagers = tbKeyManagerMapper.selectTbKeyManagerList(tbKeyManager);

        if (CollectionUtil.isNotEmpty(tbKeyManagers)){
            for(TbKeyManager tbKeyManager1:tbKeyManagers){
                Object apikey = tbKeyManager1.getSecretKey();
                String input = "1加1等于几";
                //请求URL
                String url =  getproxyUrl();

                Gpt35TurboVO gpt35TurboVO = new Gpt35TurboVO();
                gpt35TurboVO.setRole("user");
                gpt35TurboVO.setContent(input);
                List<Gpt35TurboVO> objects = new ArrayList<>();
                objects.add(gpt35TurboVO);

                Map<Object, Object> objectObjectHashMap = new HashMap<>();
                objectObjectHashMap.put("model", "gpt-3.5-turbo");
                objectObjectHashMap.put("messages", objects);
                String postData = JSONUtil.toJsonStr(objectObjectHashMap);
                try {
                    String result2 = HttpRequest.post(url)
                            .header("Authorization", "Bearer " + apikey)//头信息，多个头信息多次调用此方法即可
                            .header("Content-Type", "application/json")
                            .body(postData)//表单内容
                            .timeout(200000)//超时，毫秒
                            .execute().body();
                    if (StrUtil.isNotBlank(result2)) {
                        String error = JsonUtil.parseMiddleData(result2, "error");
                        String type = JsonUtil.parseMiddleData(error, "type");
                        if (StrUtil.equals(type, "insufficient_quota")) {
                            //用完的则更新为已刪除
                            this.changeKeyStatusToDeteleAsk(tbKeyManager1.getSecretKey());
                        }
                    }
//                    if (StrUtil.isNotBlank(result2)) {
//                        if (StrUtil.contains(result2, "statusCode") && StrUtil.contains(result2, "TooManyRequests")) {
//                            this.changeKeyStatusToDeteleAsk(tbKeyManager1.getSecretKey());
//                        }
//                    }
                }catch (Exception e){
//                    this.changeKeyStatusToDeteleAsk(tbKeyManager1.getSecretKey());
                    //异常不处理
                }
            }


        }



    }


    /**
     * 获取代理URL
     */
    @Override
    public String getproxyUrl(){
        //获取是否收录问题
        String proxy_url = iSysConfigService.selectConfigByKey("proxy_url");
        //请求URL
        String url = "https://api.openai.com/v1/chat/completions";

        if (StrUtil.isNotBlank(proxy_url) && !StrUtil.equals("0",proxy_url)){
            url = proxy_url;
        }
        return url;
    }
}
