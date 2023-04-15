package com.ruoyi.chatgpt.domain;


import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Transient;

/**
 * 角色管理对象 tb_role_table
 *
 * @author ruoyi
 * @date 2023-03-22
 */
@Data
public class TbRoleTable extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 角色名称 */
    @Excel(name = "角色名称")
    private String roleName;

    /** 角色图片 */
    @Excel(name = "角色图片")
    private String roleImage;

    /** 角色设定语 */
    @Excel(name = "角色设定语")
    private String roleContent;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private Integer isDetele;

    /** 是否可用 */
    @Excel(name = "是否可用")
    private Integer isUse;

    /** 是否选择 */
    @Transient
    private Boolean check;


    public TbRoleTable() {
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
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    public String getRoleName()
    {
        return roleName;
    }
    public void setRoleImage(String roleImage)
    {
        this.roleImage = roleImage;
    }

    public String getRoleImage()
    {
        return roleImage;
    }
    public void setRoleContent(String roleContent)
    {
        this.roleContent = roleContent;
    }

    public String getRoleContent()
    {
        return roleContent;
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
    public void setIsUse(Integer isUse)
    {
        this.isUse = isUse;
    }

    public Integer getIsUse()
    {
        return isUse;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("roleName", getRoleName())
            .append("roleImage", getRoleImage())
            .append("roleContent", getRoleContent())
            .append("userId", getUserId())
            .append("isDetele", getIsDetele())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("isUse", getIsUse())
            .toString();
    }
}
