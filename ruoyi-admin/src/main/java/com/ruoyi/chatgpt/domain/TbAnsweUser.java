package com.ruoyi.chatgpt.domain;



import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

/**
 * 小程序用户聊天列对象 tb_answe_user
 *
 * @author ruoyi
 * @date 2023-02-16
 */
@Data
public class TbAnsweUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 名单主键 */
    private Long answeUserId;

    /** 唯一ID */
    @Excel(name = "唯一ID")
    private String answeUserOpenid;

    /** 手机号 */
    private String answeUserPhone;

    /** 用户名称 */
    @Excel(name = "用户名称")
    private String answeUserName;

    /** 头像 */
    private String answeUserAvatar;

    /** 回答内容 */
        private String answeUserJson;

    /** 回答次数 */
    private Long answeUserNum;

    /** 剩余次数 */
    @Excel(name = "剩余次数")
    private Long answeUserBlanceNum;



    @Transient
    private String js_code;

    //询问内容
    @Transient
    private String prompt;


    public void setAnsweUserId(Long answeUserId)
    {
        this.answeUserId = answeUserId;
    }

    public Long getAnsweUserId()
    {
        return answeUserId;
    }
    public void setAnsweUserOpenid(String answeUserOpenid)
    {
        this.answeUserOpenid = answeUserOpenid;
    }

    public String getAnsweUserOpenid()
    {
        return answeUserOpenid;
    }
    public void setAnsweUserPhone(String answeUserPhone)
    {
        this.answeUserPhone = answeUserPhone;
    }

    public String getAnsweUserPhone()
    {
        return answeUserPhone;
    }
    public void setAnsweUserName(String answeUserName)
    {
        this.answeUserName = answeUserName;
    }

    public String getAnsweUserName()
    {
        return answeUserName;
    }
    public void setAnsweUserAvatar(String answeUserAvatar)
    {
        this.answeUserAvatar = answeUserAvatar;
    }

    public String getAnsweUserAvatar()
    {
        return answeUserAvatar;
    }
    public void setAnsweUserJson(String answeUserJson)
    {
        this.answeUserJson = answeUserJson;
    }

    public String getAnsweUserJson()
    {
        return answeUserJson;
    }
    public void setAnsweUserNum(Long answeUserNum)
    {
        this.answeUserNum = answeUserNum;
    }

    public Long getAnsweUserNum()
    {
        return answeUserNum;
    }
    public void setAnsweUserBlanceNum(Long answeUserBlanceNum)
    {
        this.answeUserBlanceNum = answeUserBlanceNum;
    }

    public Long getAnsweUserBlanceNum()
    {
        return answeUserBlanceNum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("answeUserId", getAnsweUserId())
            .append("answeUserOpenid", getAnsweUserOpenid())
            .append("answeUserPhone", getAnsweUserPhone())
            .append("answeUserName", getAnsweUserName())
            .append("answeUserAvatar", getAnsweUserAvatar())
            .append("answeUserJson", getAnsweUserJson())
            .append("answeUserNum", getAnsweUserNum())
            .append("answeUserBlanceNum", getAnsweUserBlanceNum())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
