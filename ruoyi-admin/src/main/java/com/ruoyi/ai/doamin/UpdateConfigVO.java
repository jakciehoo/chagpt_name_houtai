package com.ruoyi.ai.doamin;

import lombok.Data;

import java.util.List;

@Data
public class UpdateConfigVO {
    private List<SettingVO> formData;
    private String type;
}
