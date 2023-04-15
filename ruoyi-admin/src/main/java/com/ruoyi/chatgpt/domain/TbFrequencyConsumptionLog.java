package com.ruoyi.chatgpt.domain;


import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 次数消耗日志对象 tb_frequency_consumption_log
 *
 * @author ruoyi
 * @date 2023-03-27
 */
@Data
public class TbFrequencyConsumptionLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 操作名称 */
    @Excel(name = "操作名称")
    private String operationName;

    /** 操作类型(1:对话,2:激活卡激活-次数,3:激活卡激活-时间,4.新人邀请) */
    @Excel(name = "操作类型(1:对话,2:激活卡激活-次数,3:激活卡激活-时间,4.新人邀请)")
    private Long operationType;

    /** 对应的ID,如果对话则可以对应到具体内容 */
    @Excel(name = "对应的ID,如果对话则可以对应到具体内容")
    private String operationId;

    /** 变化内容 */
    @Excel(name = "变化内容")
    private String changeContent;

    /** 影响用户 */
    @Excel(name = "影响用户")
    private Long userId;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private Integer isDetele;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setOperationName(String operationName)
    {
        this.operationName = operationName;
    }

    public String getOperationName()
    {
        return operationName;
    }
    public void setOperationType(Long operationType)
    {
        this.operationType = operationType;
    }

    public Long getOperationType()
    {
        return operationType;
    }
    public void setOperationId(String operationId)
    {
        this.operationId = operationId;
    }

    public String getOperationId()
    {
        return operationId;
    }
    public void setChangeContent(String changeContent)
    {
        this.changeContent = changeContent;
    }

    public String getChangeContent()
    {
        return changeContent;
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
            .append("operationName", getOperationName())
            .append("operationType", getOperationType())
            .append("operationId", getOperationId())
            .append("changeContent", getChangeContent())
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
