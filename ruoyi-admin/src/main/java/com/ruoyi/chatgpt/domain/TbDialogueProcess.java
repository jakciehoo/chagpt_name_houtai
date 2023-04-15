package com.ruoyi.chatgpt.domain;


import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Transient;

/**
 * 充值卡-从对象 tb_dialogue_process
 *
 * @author ruoyi
 * @date 2023-03-27
 */
@Data
public class TbDialogueProcess extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 询问内容 */
    @Excel(name = "询问内容")
    private String askContent;

    /** 回答内容 */
    @Excel(name = "回答内容")
    private String answerContent;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 对话ID */
    @Excel(name = "对话ID")
    private Long sessionId;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private Integer isDetele;


    /** 展示个数 */
    @Transient
    private Integer limitNum;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setAskContent(String askContent)
    {
        this.askContent = askContent;
    }

    public String getAskContent()
    {
        return askContent;
    }
    public void setAnswerContent(String answerContent)
    {
        this.answerContent = answerContent;
    }

    public String getAnswerContent()
    {
        return answerContent;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setSessionId(Long sessionId)
    {
        this.sessionId = sessionId;
    }

    public Long getSessionId()
    {
        return sessionId;
    }
    public void setIsDetele(Integer isDetele)
    {
        this.isDetele = isDetele;
    }

    public Integer getIsDetele()
    {
        return isDetele;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("askContent", getAskContent())
            .append("answerContent", getAnswerContent())
            .append("userId", getUserId())
            .append("sessionId", getSessionId())
            .append("isDetele", getIsDetele())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
