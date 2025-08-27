package cn.xf.basedemo.mappers;

import cn.xf.basedemo.model.domain.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author xiongfeng
* @description 针对表【sys_permission(系统权限表 sys_permission)】的数据库操作Mapper
* @createDate 2025-08-19 21:22:03
* @Entity cn.xf.basedemo.model.domain.SysPermission
*/
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

	List<String> getPermissionListByRoleId(Long useId);

}
