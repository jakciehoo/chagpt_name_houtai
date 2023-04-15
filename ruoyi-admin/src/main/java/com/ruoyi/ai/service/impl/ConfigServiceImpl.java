package com.ruoyi.ai.service.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.ai.doamin.SettingVO;
import com.ruoyi.ai.service.IconfigService;
import com.ruoyi.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConfigServiceImpl  implements IconfigService {
    @Autowired
    private ISysConfigService iSysConfigService;

    @Override
    public String selectConfigByKey(String key)
    {
        String baseConfigJson = iSysConfigService.selectConfigByKey("baseConfigJson");
        List<SettingVO> settingVOList = JSON.parseArray(baseConfigJson, SettingVO.class);
        Map<String, String> getValue = settingVOList.stream().collect(Collectors.toMap(SettingVO::getKey, SettingVO::getValue));
        return getValue.get(key);
    }
}
