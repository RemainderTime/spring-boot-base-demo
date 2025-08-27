package cn.xf.basedemo.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 系统角色表 sys_role
 * @TableName sys_role
 */
@TableName(value ="sys_role")
@Data
public class SysRole implements Serializable {
    /**
     * 主键ID（sys_role）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称（sys_role.name）
     */
    private String name;

    /**
     * 角色标识（sys_role.code），如 ADMIN、OPERATOR
     */
    private String code;

    /**
     * 角色状态（sys_role.status），1=启用，0=禁用
     */
    private Integer status;

    /**
     * 创建时间（sys_role.create_time）
     */
    private Date create_time;

    /**
     * 创建人（sys_role.create_by）
     */
    private String create_by;

    /**
     * 更新时间（sys_role.update_time）
     */
    private Date update_time;

    /**
     * 更新人（sys_role.update_by）
     */
    private String update_by;

}