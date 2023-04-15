package com.ruoyi.ai.service.disableword;


import cn.hutool.core.util.StrUtil;
import com.ruoyi.ai.service.IconfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
//屏蔽敏感词初始化

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class SensitiveWordInit {
    //  这里使用静态，让 service 属于类
    private static IconfigService iconfigService;
    // 注入的时候，给类的 service 注入
    @Autowired
    public void setDeviceListenerService(IconfigService iconfigService) {
        SensitiveWordInit.iconfigService = iconfigService;
    }


    // 字符编码
    private String ENCODING = "UTF-8";
    // 初始化敏感字库
    public Map initKeyWord() {
// 读取敏感词库 ,存入Set中
        Set<String> wordSet = readSensitiveWordFile();
// 将敏感词库加入到HashMap中//确定有穷自动机DFA
        return addSensitiveWordToHashMap(wordSet);
    }


/**
      * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
      * 中 = {
      *      isEnd = 0
      *      国 = {
      *           isEnd = 1
      *           人 = {isEnd = 0
      *                民 = {isEnd = 1}
      *                }
      *           男  = {
      *                  isEnd = 0
      *                   人 = {
      *                        isEnd = 1
      *                       }
      *               }
      *           }
      *      }
      *  五 = {
      *      isEnd = 0
      *      星 = {
      *          isEnd = 0
      *          红 = {
      *              isEnd = 0
      *              旗 = {
      *                   isEnd = 1
      *                  }
      *              }
      *          }
      *      }
      */


    // 读取敏感词库 ,存入HashMap中
    private Set<String> readSensitiveWordFile() {
        Set<String> wordSet = null;
        String regex_rule = iconfigService.selectConfigByKey("regex_rule");
        if (StrUtil.isNotBlank(regex_rule)){
            List<String> split = StrUtil.split(regex_rule, ",");
            wordSet = split.stream().collect(Collectors.toSet());
        }
        return wordSet;
    }

    // 将HashSet中的敏感词,存入HashMap中
    private Map addSensitiveWordToHashMap(Set<String> wordSet) {


// 初始化敏感词容器，减少扩容操作
        Map wordMap = new HashMap(wordSet.size());


        for (String word : wordSet) {
            Map nowMap = wordMap;
            for (int i = 0; i < word.length(); i++) {
// 转换成char型
                char keyChar = word.charAt(i);
// 获取
                Object tempMap = nowMap.get(keyChar);
// 如果存在该key，直接赋值
                if (tempMap != null) {
                    nowMap = (Map) tempMap;
                }
// 不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                else {
// 设置标志位
                    Map<String, String> newMap = new HashMap<String, String>();
                    newMap.put("isEnd", "0");
// 添加到集合
                    nowMap.put(keyChar, newMap);
                    nowMap = newMap;
                }
// 最后一个
                if (i == word.length() - 1) {
                    nowMap.put("isEnd", "1");
                }
            }
        }
        return wordMap;
    }


}
