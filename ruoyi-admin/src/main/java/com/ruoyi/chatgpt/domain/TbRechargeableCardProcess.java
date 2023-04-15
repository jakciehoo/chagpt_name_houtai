package com.ruoyi.chatgpt.domain;


import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Transient;

/**
 * 充值卡详情对象 tb_rechargeable_card_process
 *
 * @author zhx
 * @date 2023-03-27
 */
@Data
public class TbRechargeableCardProcess extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 卡号 */
    @Excel(name = "卡号")
    private String cardNumber;

    /** 使用者ID */
    @Excel(name = "使用者ID")
    private Long userId;

    /** 批次ID */
    @Excel(name = "批次ID")
    private Long batchId;

    /** card_type - 1:次数 2:时间(分钟数) */
    @Excel(name = "充值类型(1:次数2:时间/分钟)")
    private Long cardType;

    /** 增加数量 */
    @Excel(name = "增加数量")
    private Long addNum;

    /** 增加分钟 */
    @Excel(name = "增加分钟")
    private Long addDate;



    /** 是否删除 */
    @Excel(name = "是否可用（0为可用）")
    private Integer isDetele;
    /** 生成个数 */
    @Transient
    private Integer creatNumber;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCardNumber(String cardNumber)
    {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber()
    {
        return cardNumber;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setBatchId(Long batchId)
    {
        this.batchId = batchId;
    }

    public Long getBatchId()
    {
        return batchId;
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
            .append("cardNumber", getCardNumber())
            .append("userId", getUserId())
            .append("batchId", getBatchId())
            .append("isDetele", getIsDetele())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
