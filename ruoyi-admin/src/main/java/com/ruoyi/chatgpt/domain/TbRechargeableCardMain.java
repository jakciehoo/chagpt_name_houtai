package com.ruoyi.chatgpt.domain;


import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 充值卡-主对象 tb_rechargeable_card_main
 *
 * @author ruoyi
 * @date 2023-03-27
 */
@Data
public class TbRechargeableCardMain extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 批次ID */
    @Excel(name = "批次ID")
    private Long batchId;

    /** 用户ID */
    @Excel(name = "用户ID")
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
    public void setBatchId(Long batchId)
    {
        this.batchId = batchId;
    }

    public Long getBatchId()
    {
        return batchId;
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
            .append("batchId", getBatchId())
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
