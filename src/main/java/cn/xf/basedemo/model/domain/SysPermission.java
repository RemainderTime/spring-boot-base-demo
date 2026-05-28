package cn.xf.basedemo.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 系统权限表 sys_permission
 * @TableName sys_permission
 */
@TableName(value ="sys_permission")
@Data
public class SysPermission implements Serializable {
    /**
     * 主键ID（sys_permission）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 权限标识（sys_permission.code），如 user:add、order:delete
     */
    private String code;

    /**
     * 权限名称（sys_permission.name）
     */
    private String name;

    /**
     * 所属菜单ID（sys_permission.menu_id）
     */
    private Long menuId;

    /**
     * 创建时间（sys_permission.create_time）
     */
    private Date createTime;

    /**
     * 创建人（sys_permission.create_by）
     */
    private String createBy;

    /**
     * 更新时间（sys_permission.update_time）
     */
    private Date updateTime;

    /**
     * 更新人（sys_permission.update_by）
     */
    private String updateBy;
}