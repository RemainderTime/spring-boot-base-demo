package cn.xf.basedemo.model.domain;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色菜单关联表 sys_role_menu
 * @TableName sys_role_menu
 */
@TableName(value ="sys_role_menu")
@Data
public class SysRoleMenu implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色ID（sys_role_menu.role_id）
     */
    private Long role_id;

    /**
     * 菜单ID（sys_role_menu.menu_id）
     */
    private Long menu_id;

    /**
     * 创建时间（sys_role_menu.create_time）
     */
    private Date create_time;

    /**
     * 创建人（sys_role_menu.create_by）
     */
    private String create_by;

    /**
     * 更新时间（sys_role_menu.update_time）
     */
    private Date update_time;

    /**
     * 更新人（sys_role_menu.update_by）
     */
    private String update_by;

}