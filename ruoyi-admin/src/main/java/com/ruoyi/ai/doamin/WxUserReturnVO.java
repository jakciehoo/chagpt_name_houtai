package com.ruoyi.ai.doamin;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class WxUserReturnVO {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Excel(name = "用户序号", cellType = Excel.ColumnType.NUMERIC, prompt = "用户编号")
    private Long userId;

    /** 用户账号 */
    @Excel(name = "登录名称")
    private String userName;

    /** 用户昵称 */
    @Excel(name = "用户名称")
    private String nickName;

    /** 用户邮箱 */
    @Excel(name = "用户邮箱")
    private String email;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phonenumber;

    /** 微信唯一ID */
    private String openId;


    /** 普通:1,按次:2,按时间:3 */
    private Integer vipType;

    /** 剩余回答次数 */
    private Integer blanceNum;

    /** 到期时间（到期后不可会话） */
    private Date blanceDate;

    /** 邀请码 */
    private String invitationCode;
}
