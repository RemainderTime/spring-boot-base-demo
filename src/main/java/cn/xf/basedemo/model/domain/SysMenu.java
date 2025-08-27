package cn.xf.basedemo.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 系统菜单表 sys_menu
 * @TableName sys_menu
 */
@TableName(value ="sys_menu")
@Data
public class SysMenu implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 菜单名称（sys_menu.name）
     */
    private String name;

    /**
     * 前端路由路径（sys_menu.path）
     */
    private String path;

    /**
     * 父菜单ID（sys_menu.parent_id，树结构）
     */
    private Long parent_id;

    /**
     * 菜单类型（sys_menu.type），0=目录,1=菜单,2=按钮
     */
    private Integer type;

    /**
     * 菜单图标（sys_menu.icon）
     */
    private String icon;

    /**
     * 创建时间（sys_menu.create_time）
     */
    private Date create_time;

    /**
     * 创建人（sys_menu.create_by）
     */
    private String create_by;

    /**
     * 更新时间（sys_menu.update_time）
     */
    private Date update_time;

    /**
     * 更新人（sys_menu.update_by）
     */
    private String update_by;
}