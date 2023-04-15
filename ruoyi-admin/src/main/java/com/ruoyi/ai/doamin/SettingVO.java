package com.ruoyi.ai.doamin;

import lombok.Data;

@Data
public class SettingVO {
    /**
     * 字段名称
     */
    private String name;
    /**
     * 字段类型（如果代理API等等）
     */
    private String key;
    /**
     * 字段值
     */
    private String value;
    /**
     * 字段描述
     */
    private String desc;
    /**
     * 类型
     */
    private String dataType;

}
