package com.ruoyi.ai.doamin;

import lombok.Data;

@Data
public class RegisterOrLoginVO {
    private String jsCode;
    private String inviteCode;
    private String openid;
    private String nickName;
    private String avatar;
    private String userName;
    private String valismsCode;
    private String passWord;
}
