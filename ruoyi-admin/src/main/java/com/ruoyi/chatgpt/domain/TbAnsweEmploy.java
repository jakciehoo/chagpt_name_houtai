package com.ruoyi.chatgpt.domain;



import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 回答收录列对象 tb_answe_employ
 *
 * @author ruoyi
 * @date 2023-02-10
 */
@Data
public class TbAnsweEmploy extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 名单主键 */
    private Long anserId;

    /** 询问名称 */
    @Excel(name = "询问名称")
    private String anserTitle;

    /** 询问内容 */
    @Excel(name = "询问内容")
    private String anserContent;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 是否展示 */
    @Excel(name = "是否展示")
    private Long isShow;

    public void setAnserId(Long anserId)
    {
        this.anserId = anserId;
    }

    public Long getAnserId()
    {
        return anserId;
    }
    public void setAnserTitle(String anserTitle)
    {
        this.anserTitle = anserTitle;
    }

    public String getAnserTitle()
    {
        return anserTitle;
    }
    public void setAnserContent(String anserContent)
    {
        this.anserContent = anserContent;
    }

    public String getAnserContent()
    {
        return anserContent;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setIsShow(Long isShow)
    {
        this.isShow = isShow;
    }

    public Long getIsShow()
    {
        return isShow;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("anserId", getAnserId())
            .append("anserTitle", getAnserTitle())
            .append("anserContent", getAnserContent())
            .append("userId", getUserId())
            .append("isShow", getIsShow())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
