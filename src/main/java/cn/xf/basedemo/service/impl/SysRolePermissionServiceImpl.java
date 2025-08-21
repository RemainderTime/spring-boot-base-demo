package cn.xf.basedemo.service.impl;

import cn.xf.basedemo.model.domain.SysRolePermission;
import cn.xf.basedemo.service.SysRolePermissionService;
import cn.xf.basedemo.mappers.SysRolePermissionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author xiongfeng
* @description 针对表【sys_role_permission(角色权限关联表 sys_role_permission)】的数据库操作Service实现
* @createDate 2025-08-19 21:22:03
*/
@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission>
implements SysRolePermissionService{

}
