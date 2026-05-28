package cn.xf.basedemo.interceptor;

import cn.dev33.satoken.stp.StpInterface;
import cn.xf.basedemo.mappers.SysPermissionMapper;
import cn.xf.basedemo.mappers.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private SysPermissionMapper sysPermissionMapper;

	@Autowired
	private SysRoleMapper sysRoleMapper;

	@Override
	public List<String> getPermissionList(Object userId, String s) {
		// 获取登录用户权限数据
		Long uId = Long.valueOf(userId.toString());
		List<String> permissionList = sysPermissionMapper.getPermissionListByUserId(uId);
		return permissionList;
	}

	@Override
	public List<String> getRoleList(Object userId, String s) {
		// 获取用户角色数据
		Long uId = Long.valueOf(userId.toString());
		return sysRoleMapper.getRoleListByUserId(uId);
	}
}
