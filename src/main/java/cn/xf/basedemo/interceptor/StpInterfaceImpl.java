package cn.xf.basedemo.interceptor;

import cn.dev33.satoken.stp.StpInterface;
import cn.xf.basedemo.common.utils.ApplicationContextUtils;
import cn.xf.basedemo.mappers.SysPermissionMapper;
import cn.xf.basedemo.mappers.SysRoleMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: 权限加载组件类
 * @ClassName: StpInterfaceImpl
 * @Author: xiongfeng
 * @Date: 2025/8/18 22:51
 * @Version: 1.0
 */
@Component
public class StpInterfaceImpl implements StpInterface {

	private SysPermissionMapper sysPermissionMapper =  ApplicationContextUtils.getBean(SysPermissionMapper.class);
	private SysRoleMapper sysRoleMapper =  ApplicationContextUtils.getBean(SysRoleMapper.class);
	@Override
	public List<String> getPermissionList(Object userId, String s) {
		//获取登录用户权限数据
		Long aLong = Long.valueOf(userId.toString());
		List<String> permissionList = sysPermissionMapper.getPermissionListByRoleId(aLong);
		return permissionList;
	}

	@Override
	public List<String> getRoleList(Object userId, String s) {
		//获取用户角色数据
		return sysRoleMapper.getRoleListByUserId((Long) userId);
	}
}
