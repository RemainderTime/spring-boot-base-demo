package cn.xf.basedemo.service.impl;

import cn.xf.basedemo.model.domain.SysUserRole;
import cn.xf.basedemo.service.SysUserRoleService;
import cn.xf.basedemo.mappers.SysUserRoleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author xiongfeng
* @description 针对表【sys_user_role(用户角色关联表 sys_user_role)】的数据库操作Service实现
* @createDate 2025-08-19 21:22:03
*/
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole>
implements SysUserRoleService{

}
