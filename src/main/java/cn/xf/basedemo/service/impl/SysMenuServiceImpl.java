package cn.xf.basedemo.service.impl;

import cn.xf.basedemo.model.domain.SysMenu;
import cn.xf.basedemo.service.SysMenuService;
import cn.xf.basedemo.mappers.SysMenuMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author xiongfeng
* @description 针对表【sys_menu(系统菜单表 sys_menu)】的数据库操作Service实现
* @createDate 2025-08-19 21:22:03
*/
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
implements SysMenuService{

}
