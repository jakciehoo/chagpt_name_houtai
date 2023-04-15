package com.ruoyi.ai.service;

import com.ruoyi.ai.doamin.SettingVO;
import com.ruoyi.ai.doamin.StreamParametersVO;
import com.ruoyi.ai.doamin.UpdateConfigVO;
import com.ruoyi.chatgpt.domain.TbDialogueMain;
import com.ruoyi.common.core.domain.AjaxResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface IChatGtpService {


    /**
     * 获取配置信息
     * @return
     */
    Map<String, Object> getConfigInfo();

    /**
     * 获取配置信息
     * @return
     */
    List<SettingVO> getConfigSys();

   Integer saveConfigInfo(List<SettingVO> formData);

    public Integer sysnConfigInfo(UpdateConfigVO updateConfigVO);

    /**
     * chagpt聊天流推送 - SSE
     */
    void chatBotSseStream(StreamParametersVO streamParametersVO, HttpServletResponse httpServletResponse);

    void chatSocketStream( StreamParametersVO streamParametersVO);

    /**
     * chagpt聊天流推送 - websocket
     */
    void chatBotSocketStream(StreamParametersVO streamParametersVO);


    /**
     * chagpt聊天流推送
     */
    InputStream sendRequestBefore(StreamParametersVO streamParametersVO, TbDialogueMain tbDialogueMain)  throws Exception;


    /**
     * chagpt
     */
    Map statisticsData() ;


    /**
     * 违禁词校验
     */
    void disableWordCheck(String words) ;

}
