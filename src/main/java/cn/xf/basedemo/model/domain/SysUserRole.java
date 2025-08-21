package cn.xf.basedemo.model.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户角色关联表 sys_user_role
 * @TableName sys_user_role
 */
@TableName(value ="sys_user_role")
@Data
public class SysUserRole implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID（sys_user_role.user_id）
     */
    private Long user_id;

    /**
     * 角色ID（sys_user_role.role_id）
     */
    private Long role_id;

    /**
     * 创建时间（sys_user_role.create_time）
     */
    private Date create_time;

    /**
     * 创建人（sys_user_role.create_by）
     */
    private String create_by;

    /**
     * 更新时间（sys_user_role.update_time）
     */
    private Date update_time;

    /**
     * 更新人（sys_user_role.update_by）
     */
    private String update_by;

}