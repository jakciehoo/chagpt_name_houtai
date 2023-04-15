package com.ruoyi.ai.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.ai.WeiXinUtilService;
import com.ruoyi.ai.service.disableword.SensitiveFilterService;
import com.ruoyi.ai.doamin.*;
import com.ruoyi.ai.service.IChatGtpService;
import com.ruoyi.ai.service.IExtendSysUserService;
import com.ruoyi.ai.service.IconfigService;
import com.ruoyi.chatgpt.domain.*;
import com.ruoyi.chatgpt.service.*;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.json.JsonUtil;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.util.redisson.RedisLock;
import com.ruoyi.webSocket.WebSocketService;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ruoyi.common.core.domain.AjaxResult.error;
import static com.ruoyi.common.core.domain.AjaxResult.success;

@Service
public class ChatGtpServiceImpl implements IChatGtpService {
    @Autowired
    private ISysConfigService iSysConfigService;
    @Autowired
    private IconfigService iconfigService;
    @Autowired
    private ITbModelTableService iTbModelTableService;
    @Autowired
    private ITbAnsweEmployService tbAnsweEmployService;
    @Autowired
    private ITbFrequencyConsumptionLogService iTbFrequencyConsumptionLogService;
    @Autowired(required = false)
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private WeiXinUtilService weiXinUtilService;
    @Autowired
    private ITbKeyManagerService iTbKeyManagerService;
    @Autowired
    private ITbRoleTableService tbRoleTableService;
    @Autowired
    private ITbDialogueMainService iTbDialogueMainService;

    @Autowired
    private ITbDialogueProcessService iTbDialogueProcessService;

    @Autowired
    private IExtendSysUserService iExtendSysUserService;
    @Autowired
    private ISysConfigService configService;
    @Autowired
    private RedisLock redisLock;
    @Override
    public Map<String, Object> getConfigInfo() {
        Map<String, Object> baseConfigMap = new HashMap<>();
        try {
            //首先获取存放数据的json字段
            //小程序名称
            String baseConfigJson = iSysConfigService.selectConfigByKey("baseConfigJson");
            if (StrUtil.isNotBlank(baseConfigJson)){
                List<SettingVO> settingVOList = JSON.parseArray(baseConfigJson, SettingVO.class);
                if (CollectionUtil.isNotEmpty(settingVOList)){
                    for (SettingVO settingVO: settingVOList ){
                        baseConfigMap.put(settingVO.getKey(),settingVO.getValue());
                    }
                }
            }
        }catch (Exception e){
            throw new RuntimeException("配置获取失败");
        }
        return baseConfigMap;

    }



    @Override
    public List<SettingVO> getConfigSys() {
        List<SettingVO> settingVOList = new ArrayList<>();
        try {
            //首先获取存放数据的json字段
            //小程序名称
            String baseConfigJson = iSysConfigService.selectConfigByKey("baseConfigJson");
            settingVOList = JSON.parseObject(baseConfigJson, List.class);
        }catch (Exception e){
            throw new RuntimeException("配置获取失败");
        }
        return settingVOList;

    }

    @Override
    public Integer saveConfigInfo(List<SettingVO> settingVOListEdit) {
        //获取到之前的json
        String baseConfigJson = iSysConfigService.selectConfigByKey("baseConfigJson");
        if (CollectionUtil.isEmpty(settingVOListEdit) || StrUtil.isBlank(baseConfigJson) ){
            throw new RuntimeException("保存配置失败");
        }
        List<SettingVO> settingVOListBefore = JSON.parseArray(baseConfigJson, SettingVO.class);

        Map<String, SettingVO> settingVOListBeforeMap = settingVOListBefore.stream().collect(Collectors.toMap(SettingVO::getKey, e -> e));
        if (settingVOListBefore.size()!=settingVOListEdit.size()){
            throw new RuntimeException("配置错误");
        }
       //将数据保存程默认对象,然后利用反射便利对象键值
        List<SettingVO> settingVOListResult = new ArrayList<>();
        for (SettingVO settingVO: settingVOListEdit){
            SettingVO beforeValueVO = settingVOListBeforeMap.get(settingVO.getKey());
            if (!StrUtil.equals(settingVO.getValue(),beforeValueVO.getValue())){
                SettingVO settingVTemp = new SettingVO();
                settingVTemp = beforeValueVO;
                settingVTemp.setValue(settingVO.getValue());
                settingVOListResult.add(settingVTemp);
            }else {
                settingVOListResult.add(beforeValueVO);
            }
        }
        String saveData = JSONUtil.toJsonStr(settingVOListResult);
        SysConfig baseConfigVO = iSysConfigService.selectConfigInfoByKey("baseConfigJson");
        baseConfigVO.setConfigValue(saveData);
        int i = iSysConfigService.updateConfig(baseConfigVO);
        if (i!=1){
            throw new RuntimeException("更新失败");
        }
        configService.resetConfigCache();
        return i;
    }


    @Override
    public Integer sysnConfigInfo(UpdateConfigVO updateConfigVO) {
        try{
            if (CollectionUtil.isEmpty(updateConfigVO.getFormData())){
                throw new RuntimeException("参数不可用");
            }
            if (StrUtil.isBlank(updateConfigVO.getType())){
                throw new RuntimeException("更新方式不可为空");
            }
            //获取到之前的json
            String baseConfigJson = iSysConfigService.selectConfigByKey("baseConfigJson");
            if ( StrUtil.isBlank(baseConfigJson) ){
                throw new RuntimeException("同步失败");
            }
            List<SettingVO> settingVOListBefore = JSON.parseArray(baseConfigJson, SettingVO.class);
            //获取到

            //1为:全更新   2为:增量更新
            if (StrUtil.equals(updateConfigVO.getType(),"1")){
                this.updateAll(updateConfigVO.getFormData(),settingVOListBefore);
            }else if (StrUtil.equals(updateConfigVO.getType(),"2")){
                this.updateAdd(updateConfigVO.getFormData(),settingVOListBefore);
            }
            return 1;
        }catch (Exception e){
            throw new RuntimeException("同步失败");
        }
    }


    /**
     * 全量更新-和提供的一摸一样（值不会变化）
     * @param settingVOListUpdate
     * @param settingVOListBefore
     */
    void  updateAll(List<SettingVO> settingVOListUpdate,List<SettingVO> settingVOListBefore){
        Map<String, SettingVO> settingVOListBeforeMap = settingVOListBefore.stream().collect(Collectors.toMap(SettingVO::getKey, e -> e));
        List<SettingVO> settingVOListUpdateTemp =  new ArrayList<>();
        for (SettingVO settingVO:settingVOListUpdate){
            SettingVO settingVOBefore = settingVOListBeforeMap.get(settingVO.getKey());
            if (Objects.isNull(settingVOBefore)){
                //如果为空,则说明之前是没有的
                settingVOListUpdateTemp.add(settingVO);
            }else {
                settingVOListUpdateTemp.add(settingVOBefore);
            }
        }
        String saveData = JSONUtil.toJsonStr(settingVOListUpdateTemp);
        SysConfig baseConfigVO = iSysConfigService.selectConfigInfoByKey("baseConfigJson");
        baseConfigVO.setConfigValue(saveData);
        int i = iSysConfigService.updateConfig(baseConfigVO);
        if (i!=1){
            throw new RuntimeException("同步失败");
        }
        configService.resetConfigCache();
    }

    /**
     * 增量更新-增量更新-只会增加（值不会变化）
     * @param settingVOListUpdate
     * @param settingVOListBefore
     */
    void  updateAdd(List<SettingVO> settingVOListUpdate,List<SettingVO> settingVOListBefore){
        Map<String, SettingVO> settingVOListBeforeMap = settingVOListBefore.stream().collect(Collectors.toMap(SettingVO::getKey, e -> e));

        for (SettingVO settingVO:settingVOListUpdate){
            SettingVO settingVOBefore = settingVOListBeforeMap.get(settingVO.getKey());
            if (Objects.isNull(settingVOBefore)){
                //如果为空,则说明之前是没有的
                settingVOListBefore.add(settingVO);
            }
        }
        String saveData = JSONUtil.toJsonStr(settingVOListBefore);
        SysConfig baseConfigVO = iSysConfigService.selectConfigInfoByKey("baseConfigJson");
        baseConfigVO.setConfigValue(saveData);
        int i = iSysConfigService.updateConfig(baseConfigVO);
        if (i!=1){
            throw new RuntimeException("同步失败");
        }
        configService.resetConfigCache();
    }

    @Override
    public void chatBotSseStream(StreamParametersVO streamParametersVO, HttpServletResponse httpServletResponse) {
        try {
            if (StrUtil.isBlank(streamParametersVO.getPrompt())){
                throw new RuntimeException("内容不可为空");
            }
            httpServletResponse.setContentType("text/event-stream");
            httpServletResponse.setCharacterEncoding("utf-8");
            TbDialogueMain tbDialogueMain = this.inMedothJust(streamParametersVO);
            InputStream is =  this.sendRequestBefore(streamParametersVO,tbDialogueMain);
            String line =null;
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(is));
            PrintWriter pw = httpServletResponse.getWriter();
            while((line = reader.readLine()) != null) {
//                line = line + "\n\n";
                if (StrUtil.isNotBlank(line) && !StrUtil.contains(line, "[DONE]")) {
                    line = CollectionUtil.removeEmpty(StrUtil.split(line, "data: ")).get(0);
                }
                TimeUnit.MILLISECONDS.sleep(100);
                pw.write(line);
                pw.flush();
            }
            is.close();
            pw.close();
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException("询问异常");
        } finally {
            //清楚正在问话的标识
            redisTemplate.delete(SecurityUtils.getUserId()+"");
        }
    }




    /**
     * websockt流传输 - 不支持续问
     * @param streamParametersVO
     */
    @Override
    @Transactional
    public void chatSocketStream( StreamParametersVO streamParametersVO) {
        String lockName = "aks_" + SecurityUtils.getUserId();
        try {
            RLock rLock = redisLock.getRLock(lockName);
            boolean locked = rLock.isLocked();
            if (locked){
                throw new RuntimeException("回复中");
            }
            //对同一用户访问加锁
            redisLock.lock(lockName);
            this.chatBefore(streamParametersVO);
            InputStream is = this.sendRequestBeforeChat(streamParametersVO);
            String line =null;
            String answerContent = "";
            BufferedReader reader  = new BufferedReader(new InputStreamReader(is));
            while((line = reader.readLine()) != null) {
                //首先对行数据进行处理
                if (StrUtil.isNotBlank(line) ) {
                    line = CollectionUtil.removeEmpty(StrUtil.split(line, "data: ")).get(0);
                    if (StrUtil.contains(line, "[DONE]")){
                    }else {
                        String oneWord = catchTextGpt(line);
                        if (StrUtil.isNotBlank(oneWord)){
                            answerContent = answerContent+oneWord;
                        }
                    }
                    WebSocketService.sendInfo(line,SecurityUtils.getUserId()+"");
                    TimeUnit.MILLISECONDS.sleep(50);
                }
            }
            //处理完了后将次条聊天记录进行记录
            if(StrUtil.isNotBlank(answerContent)){
                //保存聊天记录
                this.saveTbAnsweEmploy(streamParametersVO,answerContent);
            }
            is.close();
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            //解锁
            redisLock.unlock(lockName);
        }
    }

    /**
     * websockt流传输
     * @param streamParametersVO
     */
    @Override
    @Transactional
    public void chatBotSocketStream(StreamParametersVO streamParametersVO) {
        String lockName = "chat_" + SecurityUtils.getUserId();
        try {
            RLock rLock = redisLock.getRLock(lockName);
            boolean locked = rLock.isLocked();
            if (locked){
                throw new RuntimeException("回复中");
            }
            //对同一用户访问加锁
            redisLock.lock(lockName);
            //进来做校验
            TbDialogueMain tbDialogueMain = this.inMedothJust(streamParametersVO);
            String userId = SecurityUtils.getUserId()+"";
            InputStream is = this.sendRequestBefore(streamParametersVO,tbDialogueMain);
            String line =null;
            String answerContent = "";
            BufferedReader reader  = new BufferedReader(new InputStreamReader(is));
            while((line = reader.readLine()) != null) {
                //首先对行数据进行处理
                if (StrUtil.isNotBlank(line) ) {
                    line = CollectionUtil.removeEmpty(StrUtil.split(line, "data: ")).get(0);
                    if (StrUtil.contains(line, "[DONE]")){
                    }else {
                        String oneWord = catchTextGpt(line);
                        if (StrUtil.isNotBlank(oneWord)){
                            answerContent = answerContent+oneWord;
                        }
                    }
                    WebSocketService.sendInfo(line,userId);
                    TimeUnit.MILLISECONDS.sleep(50);
                }
            }
            //处理完了后将次条聊天记录进行记录
            if(StrUtil.isNotBlank(answerContent)){
                //保存聊天记录
                this.saveDig(streamParametersVO,answerContent);
            }
            is.close();
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            //解锁
            redisLock.unlock(lockName);
            //清楚正在问话的标识
            redisTemplate.delete(SecurityUtils.getUserId()+"");
        }
    }

    /**
     * 交谈前的
     * @param streamParametersVO
     * @return
     */
    @Transactional
    public void chatBefore(StreamParametersVO streamParametersVO){
        //方法刚进来时候的判断
        if (StrUtil.isBlank(streamParametersVO.getPrompt())){
            throw new RuntimeException("内容不可为空");
        }
        //是否免费,问询接口
        String is_open_ask_check = iconfigService.selectConfigByKey("is_open_ask_check");
        if (StrUtil.isNotBlank(is_open_ask_check) && StrUtil.equals(is_open_ask_check,"1") ){
            iExtendSysUserService.numOrDateAddOrDe();
        }
    }

    /**
     * 校验参数是否可以为空 -- 续问模式问答前校验
     * @param streamParametersVO
     * @return
     */
    @Transactional
    public TbDialogueMain inMedothJust(StreamParametersVO streamParametersVO){
        //方法刚进来时候的判断
        if (StrUtil.isBlank(streamParametersVO.getPrompt())){
            throw new RuntimeException("内容不可为空");
        }
        //判断是否存在违禁词
        this.catchDisableWordRegx(streamParametersVO.getPrompt());
        //首先根据对话主表ID查询是否有改对话内容
        //对话主表ID
        Long dialogueId = streamParametersVO.getDialogueId();
        if (Objects.isNull(dialogueId)){
            throw new RuntimeException("请新建对话");
        }
        //查询对话是否存在
        TbDialogueMain tbDialogueMainParam = new TbDialogueMain();
        tbDialogueMainParam.setId(dialogueId);
        tbDialogueMainParam.setUserId(SecurityUtils.getUserId());
        List<TbDialogueMain> tbDialogueMains = iTbDialogueMainService.selectTbDialogueMainList(tbDialogueMainParam);
        if (CollectionUtil.isEmpty(tbDialogueMains)){
            throw new RuntimeException("请新建对话");
        }

        //次数或时间校验以及扣除
        String open_api_key = iconfigService.selectConfigByKey("is_open_num");
        if (StrUtil.isNotBlank(open_api_key) && StrUtil.equals(open_api_key,"1") ){
            iExtendSysUserService.numOrDateAddOrDe();
        }
        return tbDialogueMains.get(0);
    }


    /**
     * 咨询完以后保存问题以及信息
     * @param streamParametersVO
     * @return
     */
    @Transactional
    public void saveTbAnsweEmploy(StreamParametersVO streamParametersVO,String answerContent){
        //插入对话列表
        TbAnsweEmploy tbAnsweEmploy = new TbAnsweEmploy();
        tbAnsweEmploy.setAnserTitle(streamParametersVO.getPrompt());
        tbAnsweEmploy.setAnserContent(answerContent);
        try {
            tbAnsweEmploy.setUserId(SecurityUtils.getUserId());
        }catch (Exception e){
            tbAnsweEmploy.setUserId(null);
        }
        int i = tbAnsweEmployService.insertTbAnsweEmploy(tbAnsweEmploy);
        if (i<1){
            throw new RuntimeException("答案保存失败");
        }
    }


    /**
     * 咨询完以后保存问题以及信息
     * @param streamParametersVO
     * @return
     */
    @Transactional
    public void saveDig(StreamParametersVO streamParametersVO,String answerContent){
        //插入对话列表
        TbDialogueProcess tbDialogueProcess = new TbDialogueProcess();
        tbDialogueProcess.setAskContent(streamParametersVO.getPrompt());
        tbDialogueProcess.setAnswerContent(answerContent);
        tbDialogueProcess.setUserId(SecurityUtils.getUserId());
        tbDialogueProcess.setSessionId(streamParametersVO.getDialogueId());
        //封装查询方法

        int i = iTbDialogueProcessService.insertTbDialogueProcess(tbDialogueProcess);
        if (i!=1){
            throw new RuntimeException("对话保存失败");
        }
        //封装日志记录
        TbFrequencyConsumptionLog tbFrequencyConsumptionLog = new TbFrequencyConsumptionLog();
        tbFrequencyConsumptionLog.setOperationName("对话扣除");
        tbFrequencyConsumptionLog.setChangeContent("提问【"+streamParametersVO.getPrompt()+"】扣除一次");
        tbFrequencyConsumptionLog.setOperationType(1l);
        tbFrequencyConsumptionLog.setOperationId(streamParametersVO.getDialogueId()+"");
        tbFrequencyConsumptionLog.setRemark("1");
        iTbFrequencyConsumptionLogService.insertTbFrequencyConsumptionLog(tbFrequencyConsumptionLog);
    }


    /**
     * 这块为问询,不包含对话模式
     * @param streamParametersVO
     * @return
     * @throws Exception
     */
    @Transactional
    public InputStream sendRequestBeforeChat(StreamParametersVO streamParametersVO) throws Exception {
        InputStream in = null;
        // 通知内容添加文本铭感词汇过滤
        //其余错误见返回码说明
        //正常返回0
        //违禁词检测
        this.disableWordCheck(streamParametersVO.getPrompt());
        String apikeyRefresh = getOpenAiKey();
        if (StrUtil.isBlank(apikeyRefresh)){
            throw new RuntimeException("无可用key");
        }
        List<Gpt35TurboVO> chatContext = this.getChatContext(streamParametersVO);
        String requestUrl = iTbKeyManagerService.getproxyUrl();
        Map<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("model", "gpt-3.5-turbo");
        objectObjectHashMap.put("messages", chatContext);
        objectObjectHashMap.put("stream", true);
        objectObjectHashMap.put("temperature", 0);
        objectObjectHashMap.put("frequency_penalty", 0);
        objectObjectHashMap.put("presence_penalty", 0.6);
        String bodyJson = JSONUtil.toJsonStr(objectObjectHashMap);
        URL url = new URL(requestUrl); // 接口地址
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Connection", "Keep-Alive");
        urlConnection.setRequestProperty("Charset", "UTF-8");
        urlConnection.setRequestProperty("Authorization", "Bearer " + apikeyRefresh);
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        byte[] dataBytes = bodyJson.getBytes();
        urlConnection.setRequestProperty("Content-Length", String.valueOf(dataBytes.length));
        OutputStream os = urlConnection.getOutputStream();
        os.write(dataBytes);
        in =   new BufferedInputStream(urlConnection.getInputStream());
        os.flush();
        os.close();
        return in;

    }

    /**
     * 这块为 - 流对话模式的封装
     * @param streamParametersVO
     * @param tbDialogueMain
     * @return
     * @throws Exception
     */
    @Transactional
    public InputStream sendRequestBefore(StreamParametersVO streamParametersVO,TbDialogueMain tbDialogueMain) throws Exception {
        InputStream in = null;
            String prompt = streamParametersVO.getPrompt();
            // 获取当前的用户
            String userId = SecurityUtils.getUserId()+"";
            Object o = redisTemplate.opsForValue().get(userId);
            if (!Objects.isNull(o)) {
                throw new RuntimeException("正在回复");
            }
            redisTemplate.opsForValue().set(userId, true, 30, TimeUnit.SECONDS);
            if (StrUtil.isBlank(prompt)) {
                throw new RuntimeException("输入内容为空");
            }
            // 通知内容添加文本铭感词汇过滤
            //其余错误见返回码说明
            //正常返回0
            //违禁词检测
            this.disableWordCheck(prompt);
            String apikeyRefresh = getOpenAiKey();
            if (StrUtil.isBlank(apikeyRefresh)){
                throw new RuntimeException("无可用key");
            }
            List<Gpt35TurboVO> chatContext = this.getChatDigContext(streamParametersVO,tbDialogueMain);
            String requestUrl = iTbKeyManagerService.getproxyUrl();
            Map<Object, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("model", "gpt-3.5-turbo");
            objectObjectHashMap.put("messages", chatContext);
            objectObjectHashMap.put("stream", true);
            objectObjectHashMap.put("temperature", 0);
            objectObjectHashMap.put("frequency_penalty", 0);
            objectObjectHashMap.put("presence_penalty", 0.6);
            String bodyJson = JSONUtil.toJsonStr(objectObjectHashMap);
            URL url = new URL(requestUrl); // 接口地址
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Charset", "UTF-8");
            urlConnection.setRequestProperty("Authorization", "Bearer " + apikeyRefresh);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            byte[] dataBytes = bodyJson.getBytes();
            urlConnection.setRequestProperty("Content-Length", String.valueOf(dataBytes.length));
            OutputStream os = urlConnection.getOutputStream();
            os.write(dataBytes);
             in =   new BufferedInputStream(urlConnection.getInputStream());
            os.flush();
            os.close();
            return in;

    }

    @Override
    public Map statisticsData() {
        //统计会话次数
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        int dialogueCount = iTbDialogueProcessService.getDialogueCount();
        int intivateCount = iTbFrequencyConsumptionLogService.getIntivateCount(SecurityUtils.getUserId());
        objectObjectHashMap.put("dialogueCount",dialogueCount);
        objectObjectHashMap.put("intivateCount",intivateCount);
        return objectObjectHashMap;
    }

    @Override
    public void disableWordCheck(String words) {
        Integer isSensitive = weiXinUtilService.msgCheck(words);
        //当图片文件内含有敏感内容，则返回87014
        if (Objects.isNull(isSensitive)) {
            throw new RuntimeException("违禁词验证失败");
        } else if (isSensitive == 87014) {
            throw new RuntimeException("含有违禁词");
        } else if (isSensitive != 0) {
            throw new RuntimeException("违禁词错误");
        }
    }

    public String getOpenAiKey() {
        //模仿查到的key集合
        TbKeyManager tbKeyManager = new TbKeyManager();
        tbKeyManager.setIsUse(1);
        //可用的key
        List<TbKeyManager> tbKeyManagers = iTbKeyManagerService.selectTbKeyManagerList(tbKeyManager);
        //判断是否key额度用完
        if (CollectionUtil.isEmpty(tbKeyManagers) || tbKeyManagers.size() <= 0) {
            throw new RuntimeException("key额度耗尽");
        }
        //获取第一个key,然后将第一个key存入缓存
        String key = tbKeyManagers.get(0).getSecretKey();
        redisTemplate.opsForValue().set("apikey", key);
        //检查key
        changeKey(tbKeyManagers.get(0));
        return key;
    }

    public void changeKey( TbKeyManager tbKeyManager) {
        String apikey = tbKeyManager.getSecretKey();
        String input = "1加1等于几";
        //请求URL
        String url = iTbKeyManagerService.getproxyUrl();
        Gpt35TurboVO gpt35TurboVO = new Gpt35TurboVO();
        gpt35TurboVO.setRole("user");
        gpt35TurboVO.setContent(input);
        List<Gpt35TurboVO> objects = new ArrayList<>();
        objects.add(gpt35TurboVO);
        Map<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("model", "gpt-3.5-turbo");
        objectObjectHashMap.put("messages", objects);
        String postData = JSONUtil.toJsonStr(objectObjectHashMap);
        String body = HttpRequest.post(url)
                .header("Authorization", "Bearer " + apikey)//头信息，多个头信息多次调用此方法即可
                .header("Content-Type", "application/json")
                .body(postData)//表单内容
                .timeout(200000)//超时，毫秒
                .execute().body();
        this.checkBodyChangeResult(body, apikey);
    }
    /**
     * 对检查key是否有效的方法
     * @param body
     * @return
     */
    public void checkBodyChangeResult(String body,String apikey){
        if (StrUtil.isBlank(body)) {
            throw new RuntimeException("访问错误");
        }
        String error = JsonUtil.parseMiddleData(body, "error");
        //出错类型
        String type = JsonUtil.parseMiddleData(error, "type");
        //次数耗尽
        if (StrUtil.equals(type, "insufficient_quota")) {
            redisTemplate.delete("apikey");
            iTbKeyManagerService.changeKeyStatusToUsed(apikey);
            getOpenAiKey();
        }

        if (StrUtil.contains(body, "invalid_request_error")) {
            String errorResult = JsonUtil.parseMiddleData(body, "error");
            String typeResult = JsonUtil.parseMiddleData(errorResult, "type");
            String code = JsonUtil.parseMiddleData(errorResult, "code");
            if (StrUtil.equals(typeResult, "invalid_request_error") && StrUtil.equals(code, "account_deactivated")) {
                redisTemplate.delete("apikey");
                iTbKeyManagerService.changeKeyStatusToUsed(apikey);
                getOpenAiKey();
            }else if (StrUtil.equals(typeResult, "invalid_request_error") && StrUtil.equals(code, "invalid_api_key")) {
                redisTemplate.delete("apikey");
                iTbKeyManagerService.changeKeyStatusToUsed(apikey);
                getOpenAiKey();
            }
        }
        if (StrUtil.contains(body, "statusCode") && StrUtil.contains(body, "TooManyRequests")) {
            redisTemplate.delete("apikey");
            getOpenAiKey();
        }
        if (StrUtil.contains(body, "code") && Objects.isNull(JsonUtil.parseMiddleData(body, "code")) && StrUtil.contains(body, "insufficient_quota")) {
            redisTemplate.delete("apikey");
            iTbKeyManagerService.changeKeyStatusToUsed(apikey);
            getOpenAiKey();
        }
    }

    /**
     * 处理单独打印的文字
     * @param str
     * @return
     */
    public String catchTextGpt(String str){
        String choices = JsonUtil.parseMiddleData(str, "choices");
        JSONArray jsonArray = JSONUtil.parseArray(choices);
        String string = jsonArray.getByPath("[0].delta").toString();
        String content = JsonUtil.parseMiddleData(string, "content");
        return content;
    }




    public List<Gpt35TurboVO> getChatContext(StreamParametersVO streamParametersVO){
        Gpt35TurboVO gpt35TurboVOSys = new Gpt35TurboVO();
        gpt35TurboVOSys.setRole("system");
        gpt35TurboVOSys.setContent("你是一个可以问答任何问题的全能机器人");

        Gpt35TurboVO gpt35TurboVO = new Gpt35TurboVO();
        gpt35TurboVO.setRole("user");
        gpt35TurboVO.setContent(streamParametersVO.getPrompt());
        List<Gpt35TurboVO> objects = new ArrayList<>();
        objects.add(gpt35TurboVOSys);
        objects.add(gpt35TurboVO);
        return objects;
    }

    /**
     * 获取对话上下文
     * @param streamParametersVO
     * @return
     */
    public List<Gpt35TurboVO> getChatDigContext(StreamParametersVO streamParametersVO,TbDialogueMain tbDialogueMain){
        List<Gpt35TurboVO> messages = new ArrayList<>();
        //首先获取角色,默认角色
        String default_role = iconfigService.selectConfigByKey("default_role");
        if (StrUtil.isBlank(default_role)){
            default_role ="我是一个全能机器人,可以回答你任何问题";
        }

        TbModelTable tbModelTable = iTbModelTableService.selectTbModelTableById(tbDialogueMain.getDialogueRoleId());
        if (!Objects.isNull(tbModelTable)){
            default_role = tbModelTable.getModelContent();
        }
        //设定系统所扮演的角色
        Gpt35TurboVO gpt35TurboVOSys = new Gpt35TurboVO();
        gpt35TurboVOSys.setRole("system");
        gpt35TurboVOSys.setContent(default_role);
        messages.add(gpt35TurboVOSys);
        //然后获取系统数据TbDialogueProcess
        TbDialogueProcess tbDialogueProcess = new TbDialogueProcess();
        tbDialogueProcess.setSessionId(streamParametersVO.getDialogueId());
        tbDialogueProcess.setUserId(SecurityUtils.getUserId());
        String default_context_num = iconfigService.selectConfigByKey("default_context_num");
        if (StrUtil.isBlank(default_context_num) || !NumberUtil.isNumber(default_context_num)){
            default_context_num ="10";
        }
        tbDialogueProcess.setLimitNum(Integer.valueOf(default_context_num));
        //根据对话ID和用户ID查询到对话列表-根据时间倒叙获取后几条设定的数据
        //首先获取角色,默认角色
        List<TbDialogueProcess> tbDialogueProcessesDesc = iTbDialogueProcessService.selectTbDialogueProcessListByLimitDesc(tbDialogueProcess);
        if (CollectionUtil.isNotEmpty(tbDialogueProcessesDesc)){
            //获取到倒数10条数据后将数据正序配好
            List<TbDialogueProcess> tbDialogueProcesses = tbDialogueProcessesDesc.stream().sorted(Comparator.comparing(TbDialogueProcess::getCreateTime)).collect(Collectors.toList());
            for (TbDialogueProcess tbDialogueProcessfor:tbDialogueProcesses){
                Gpt35TurboVO gpt35TurboUser = new Gpt35TurboVO();
                //用户询问的问题
                gpt35TurboUser.setRole("user");
                gpt35TurboUser.setContent(tbDialogueProcessfor.getAskContent());
                messages.add(gpt35TurboUser);
                //机器人回答的问题
                Gpt35TurboVO gpt35TurAssistant = new Gpt35TurboVO();
                gpt35TurAssistant.setRole("assistant");
                gpt35TurAssistant.setContent(tbDialogueProcessfor.getAnswerContent());
                messages.add(gpt35TurAssistant);
            }
        }

        //最后查询用户询问的问题
        Gpt35TurboVO gpt35TurboUser = new Gpt35TurboVO();
        gpt35TurboUser.setRole("user");
        gpt35TurboUser.setContent(streamParametersVO.getPrompt());
        messages.add(gpt35TurboUser);
        return messages;
    }


    /**
     * 正则匹配是否违规
     * @param promt  - 输入的话
     */
    public void catchDisableWordRegx(String promt){
        SensitiveFilterService filter = SensitiveFilterService.getInstance();
        boolean check =  filter.checkContainCount(promt);
        if(check){
            throw new RuntimeException("存在违禁词");
        }
    }

}
