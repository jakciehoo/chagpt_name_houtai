package com.ruoyi.ai.doamin;

import com.ruoyi.ai.doamin.ContentVo;
import lombok.Data;

import java.util.Date;

@Data
public class ReturnAnswerVo {
    private Boolean  animation;
    /*
    内容类型,1默认文字
     */
    private Integer contentType;
    /*
     1是自己提问的,2是机器人
    */
    private Integer fromId;
    /**
     * 内容
     */
    private ContentVo content;
    /**
     * 回答时间
     */
    private Date answerTime;
}


