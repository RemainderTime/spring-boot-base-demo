package cn.xf.basedemo.service.impl;

import cn.xf.basedemo.model.domain.SysPermission;
import cn.xf.basedemo.service.SysPermissionService;
import cn.xf.basedemo.mappers.SysPermissionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author xiongfeng
* @description 针对表【sys_permission(系统权限表 sys_permission)】的数据库操作Service实现
* @createDate 2025-08-19 21:22:03
*/
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission>
implements SysPermissionService{

}
