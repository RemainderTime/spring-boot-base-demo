package cn.xf.basedemo.model.domain;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色权限关联表 sys_role_permission
 * @TableName sys_role_permission
 */
@TableName(value ="sys_role_permission")
@Data
public class SysRolePermission implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色ID（sys_role_permission.role_id）
     */
    private Long role_id;

    /**
     * 权限ID（sys_role_permission.permission_id）
     */
    private Long permission_id;

    /**
     * 创建时间（sys_role_permission.create_time）
     */
    private Date create_time;

    /**
     * 创建人（sys_role_permission.create_by）
     */
    private String create_by;

    /**
     * 更新时间（sys_role_permission.update_time）
     */
    private Date update_time;

    /**
     * 更新人（sys_role_permission.update_by）
     */
    private String update_by;

}