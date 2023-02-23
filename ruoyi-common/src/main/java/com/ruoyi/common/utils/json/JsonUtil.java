package com.ruoyi.common.utils.json;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;


public class JsonUtil {
    /**
     * 处理{}这种类型的数据
     *
     * @param json
     * @return
     */
    public static String parseMiddleData(String json, String parse) {
        JSONObject jsonObject = JSONUtil.parseObj(json);
        String str = jsonObject.getStr(parse);
        return str;
    }
}
