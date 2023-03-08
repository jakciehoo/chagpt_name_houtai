package com.ruoyi.ai;

import lombok.Data;

@Data
public class Gpt35TurboVO {
    private String role;  //角色一般为 user
    private String content;  //角色一般为 询问内容
}
