package cn.xf.basedemo.service.impl;

import cn.xf.basedemo.model.domain.SysRole;
import cn.xf.basedemo.service.SysRoleService;
import cn.xf.basedemo.mappers.SysRoleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author xiongfeng
* @description 针对表【sys_role(系统角色表 sys_role)】的数据库操作Service实现
* @createDate 2025-08-19 21:22:03
*/
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
implements SysRoleService{

}
