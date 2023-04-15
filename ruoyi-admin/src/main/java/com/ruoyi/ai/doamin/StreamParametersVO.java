package com.ruoyi.ai.doamin;

import lombok.Data;

import java.io.OutputStream;

@Data
public class StreamParametersVO {
    /**
     * 输入内容
     */
    private String prompt;
    /**
     * 页面
     */
    private String page;
    /**
     * 个数
     */
    private String size;


    /**
     * 对话ID
     */
    private  Long dialogueId;

    /**
     * 对话名称
     */
    private  String dialogueName;

    /**
     * 角色ID
     */
    private  Integer dialogueRoleId;

}
