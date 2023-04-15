package com.ruoyi.chatgpt.domain;


import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Transient;

/**
 * 对话列-主对象 tb_dialogue_main
 *
 * @author ruoyi
 * @date 2023-03-27
 */
@Data
public class TbDialogueMain extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 对话角色ID */
    private Long dialogueRoleId;

    /** 话题名称 */
    @Excel(name = "话题名称")
    private String dialogueName;

    /** 对话ID */
    @Excel(name = "对话ID")
    private String sessionId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private Integer isDetele;

    /**
     * 对话数量
     */
    @Transient
    private Long digNum;

    @Transient
    private String modelName;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setDialogueName(String dialogueName)
    {
        this.dialogueName = dialogueName;
    }

    public String getDialogueName()
    {
        return dialogueName;
    }
    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public String getSessionId()
    {
        return sessionId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
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
            .append("dialogueName", getDialogueName())
            .append("sessionId", getSessionId())
            .append("userId", getUserId())
            .append("isDetele", getIsDetele())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
