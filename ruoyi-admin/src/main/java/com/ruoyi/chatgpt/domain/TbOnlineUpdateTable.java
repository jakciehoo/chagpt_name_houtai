package com.ruoyi.chatgpt.domain;


import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 在线更新对象 tb_online_update_table
 *
 * @author ruoyi
 * @date 2023-03-22
 */
@Data
public class TbOnlineUpdateTable extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 在线版本名称 */

    private String onlineVersionName;

    /** 在线版本描述 */

    private String onlineVersionDesc;

    /** 在线版本号 */

    private String onlineVersionNumber;

    /** 执行sql */

    private String onlineLastSql;

    /** 能够在线更新 */

    private Long isOnlineUpdate;

    /** 是否删除 */

    private Integer isDetele;
}
