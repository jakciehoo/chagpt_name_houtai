package com.ruoyi.quartz.domain;

import cn.hutool.json.JSONObject;
import lombok.Data;

@Data
public class ReturnSuccessCode {
    private Integer code;
    private JSONObject responseBody;
}
