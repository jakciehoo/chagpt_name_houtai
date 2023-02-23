package com.ruoyi.quartz.domain;

import lombok.Data;
import org.apache.http.NameValuePair;

import java.util.List;

@Data
public class KjRequestTaskParamsVO {
//    //用户创建的平台ID
//    private Long ctPlatformId;
    //捡漏类型  1 : 捡到为止  2 : 捡漏次
    private Integer rushPurchaseType;
//    //捡漏类型为2时捡漏次数
//    private Integer rushPurchaseNumber;
    //请求类型 1为get请求 2为post请求
    private Integer requestType;
    //请求参数
    private List<NameValuePair> params;
}
