package com.ruoyi.chatgpt.domain;



import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * key管理对象 tb_key_manager
 *
 * @author ruoyi
 * @date 2023-03-06
 */
@Data
public class TbKeyManager extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** key值 */
    @Excel(name = "key值")
    private String secretKey;

    /** 用户ID */
    private Long userId;

    /** 是否可用 */
    @Excel(name = "是否可用")
    private Integer isUse;

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
    public void setSecretKey(String secretKey)
    {
        this.secretKey = secretKey;
    }

    public String getSecretKey()
    {
        return secretKey;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setIsUse(Integer isUse)
    {
        this.isUse = isUse;
    }

    public Integer getIsUse()
    {
        return isUse;
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
            .append("secretKey", getSecretKey())
            .append("userId", getUserId())
            .append("isUse", getIsUse())
            .append("isDetele", getIsDetele())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
