package com.ruoyi.chatgpt.domain;



import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

/**
 * 模型建设对象 tb_model_table
 *
 * @author zhx
 * @date 2023-04-11
 */
@Data
public class TbModelTable extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 主键 对话角色ID d_role_id*/
    private Long  dRoleId;

    /** 模型名称 */
    @Excel(name = "模型名称")
    private String modelName;

    /** 模型图片 */
    @Excel(name = "模型图片")
    private String modelImage;

    /** 模型设定语 */
    @Excel(name = "模型设定语")
    private String modelContent;

    /** 是否可用 */
    @Excel(name = "是否可用")
    private Integer isUse;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private Integer isDetele;


    /** 是否选择 */
    @Transient
    private Boolean check;


    public TbModelTable() {
        super();
        this.check=false;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setModelName(String modelName)
    {
        this.modelName = modelName;
    }

    public String getModelName()
    {
        return modelName;
    }
    public void setModelImage(String modelImage)
    {
        this.modelImage = modelImage;
    }

    public String getModelImage()
    {
        return modelImage;
    }
    public void setModelContent(String modelContent)
    {
        this.modelContent = modelContent;
    }

    public String getModelContent()
    {
        return modelContent;
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
            .append("modelName", getModelName())
            .append("modelImage", getModelImage())
            .append("modelContent", getModelContent())
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
