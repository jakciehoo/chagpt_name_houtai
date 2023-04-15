package com.ruoyi.ai.doamin;

import lombok.Data;

@Data
public class RequestKeyVO {
    private  String model; //可选参数。语言模型，这里选择的是text-davinci-003 / gpt-3.5-turbo
    private  String prompt;//必选参数。即用户的输入。
    private  String max_tokens;//可选参数，默认值为 16。最大分词数，会影响返回结果的长度。
    private  String temperature;//可选参数，默认值为 1，取值 0-2。该值越大每次返回的结果越随机，即相似度越小。
    private  String top_p;//可选参数，与temperature类似。
    private  String n;//可选参数，默认值为 1。表示对每条prompt生成多少条结果。
    private  String stream;//可选参数，默认值为false。表示是否回流部分结果。
    private  String key;//必选参数key。
}
