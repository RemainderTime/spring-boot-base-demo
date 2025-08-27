package cn.xf.basedemo.mappers;

import cn.xf.basedemo.model.domain.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author xiongfeng
* @description 针对表【sys_role(系统角色表 sys_role)】的数据库操作Mapper
* @createDate 2025-08-19 21:22:03
* @Entity cn.xf.basedemo.model.domain.SysRole
*/
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

	List<String> getRoleListByUserId(Integer userId);


}
