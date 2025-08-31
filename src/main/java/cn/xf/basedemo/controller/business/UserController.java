package cn.xf.basedemo.controller.business;

import cn.xf.basedemo.common.model.CustomUserDetails;
import cn.xf.basedemo.common.model.LoginUser;
import cn.xf.basedemo.common.model.RetObj;
import cn.xf.basedemo.interceptor.SessionContext;
import cn.xf.basedemo.model.res.LoginInfoRes;
import cn.xf.basedemo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * @program: xf-boot-base
 * @ClassName UserController
 * @description: 用户控制器
 * @author: xiongfeng
 * @create: 2022-06-28 09:17
 **/
@RestController(value = "用户控制器")
@RequestMapping("/user")
@Tag(name = "用户控制器")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "用户登录", description = "用户登录")
    @PostMapping("/login")
    public RetObj login(@RequestBody LoginInfoRes res){

        return userService.login(res);
    }

    @Operation(summary = "用户信息", description = "用户信息")
    @PostMapping("/info")
    @PreAuthorize("hasAuthority('user:add')") // 权限控制
    public RetObj info(){
//        LoginUser loginUser = SessionContext.getInstance().get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return RetObj.success(user);
    }

    @Operation(summary = "es同步用户信息", description = "用户信息")
    @GetMapping("/syncEs")
    @PreAuthorize("hasRole('admin')") // 角色控制
    public RetObj syncEs(Long userId){
        return userService.syncEs(userId);
    }

    @Operation(summary = "es查询用户信息", description = "用户信息")
    @GetMapping("/getEsId")
    public RetObj getEsId(Long userId){
        return userService.getEsId(userId);
    }
    @Operation(summary = "获取用户权限数据", description = "用户信息")
    @GetMapping("/getPermission")
    public RetObj getPermission(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return RetObj.success(user.getAuthorities());
    }


}
