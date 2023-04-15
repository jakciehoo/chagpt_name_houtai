package com.ruoyi.ai.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.ai.service.IconfigService;
import com.ruoyi.chatgpt.domain.TbOnlineUpdateTable;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.json.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在线更新
 */
@RequestMapping("/online")
@RestController
public class OnlineUpdateController {
    @Autowired
    private IconfigService iconfigService;

    @GetMapping("/update/getversionList")
    @Anonymous
    public AjaxResult registerOrLogin()
    {
        //获取默认的用户头像
        String online_update_url = iconfigService.selectConfigByKey("online_update_url");
        String pro_version = iconfigService.selectConfigByKey("pro_version");

        Map<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("model", "gpt-3.5-turbo");
        objectObjectHashMap.put("stream", true);
        String postData = JSONUtil.toJsonStr(objectObjectHashMap);
        String body = HttpRequest.get(online_update_url)
                .header("Content-Type", "application/json")
                .body(postData)//表单内容
                .timeout(200000)//超时，毫秒
                .execute().body();
        String rows = JsonUtil.parseMiddleData(body, "rows");
        List list = JSON.parseObject(rows, List.class);
        return  AjaxResult.success(list);
    }

    @GetMapping("/update/version")
    @Anonymous
    public AjaxResult registerOrLoginis()
    {
        String online_update_url = iconfigService.selectConfigByKey("online_update_url");

        String pro_version = iconfigService.selectConfigByKey("pro_version");

        String body = HttpRequest.get(online_update_url)
                .header("Content-Type", "application/json")
                .timeout(200000)//超时，毫秒
                .execute().body();
        String rows = JsonUtil.parseMiddleData(body, "rows");
        List<TbOnlineUpdateTable> list = JSON.parseArray(rows, TbOnlineUpdateTable.class);
        TbOnlineUpdateTable tbOnlineUpdateTable = list.get(0);
        String onlineVersionNumber = tbOnlineUpdateTable.getOnlineVersionNumber();
        Integer integer = compareVersion(pro_version, onlineVersionNumber);
        return  AjaxResult.success(integer);
    }





    public static Integer compareVersion(String version1, String version2) {
        try {
            if (version1 == null || version2 == null) {
                throw new IllegalArgumentException("Bad version number");
            }
            String[] versionArray1 = version1.split("\\.");
            String[] versionArray2 = version2.split("\\.");
            if (versionArray1.length != 3 || versionArray2.length != 3) {
                throw new IllegalArgumentException("Bad version number");
            }
            // 逐段比较版本号，先比较第一位
            Integer result = null;
            for (int i = 0; i < 3; i++) {
                Integer v1 = Integer.parseInt(versionArray1[i]);
                Integer v2 = Integer.parseInt(versionArray2[i]);
                result = Integer.compare(v1 - v2, 0);
                if (result != 0) {
                    break;
                }
            }
            return result;
        } catch (Exception ex) {
//            log.error("Error to compare version", ex);
            return -2;
        }
    }


}
