package com.ruoyi.ai;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ReturnAnswerVo {
    private Boolean  animation;
    /*
    内容类型,1默认文字
     */
    private Integer contentType;
    /*
     1是自己提问的,2是机器人
    */
    private Integer fromId;
//    /**
//     * 提问名称
//     */
//    private String name;
//    /**
//     * 头像
//     */
//    private String avatarUrl;
    /**
     * 内容
     */
    private ContentVo content;
    /**
     * 回答时间
     */
    private Date answerTime;
}


@Data
class ContentVo {
    private  String text;

}




@Data
class Gpt35TurboVO {
    private String role;  //角色一般为 user
    private String content;  //角色一般为 询问内容
}
