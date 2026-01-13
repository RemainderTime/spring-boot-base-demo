package cn.xf.basedemo.controller.business;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.annotation.SaMode;
import cn.xf.basedemo.common.model.LoginUser;
import cn.xf.basedemo.common.model.RetObj;
import cn.xf.basedemo.interceptor.SessionContext;
import cn.xf.basedemo.model.res.LoginInfoRes;
import cn.xf.basedemo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
    public RetObj login(@RequestBody @Validated LoginInfoRes res) {

        return userService.login(res);
    }

    @Operation(summary = "用户信息", description = "用户信息")
    @PostMapping("/info")
    @SaCheckPermission("user:info") // 权限校验
    public RetObj info() {
        LoginUser loginUser = SessionContext.getInstance().get();
        return RetObj.success(loginUser);
    }

    @Operation(summary = "注解示例-角色校验", description = "必须具有 'super-admin' 角色才能访问")
    @PostMapping("/check-role")
    @SaCheckRole("super-admin")
    public RetObj checkRole() {
        return RetObj.success("您拥有 super-admin 角色，验证通过");
    }

    @Operation(summary = "注解示例-权限组合(OR)", description = "只要拥有 user:add 或 user:update 其中一个权限即可")
    @PostMapping("/check-permission-or")
    @SaCheckPermission(value = { "user:add", "user:update" }, mode = SaMode.OR)
    public RetObj checkPermissionOr() {
        return RetObj.success("您拥有 user:add 或 user:update 权限，验证通过");
    }

    @Operation(summary = "注解示例-权限组合(AND)", description = "必须同时拥有 user:delete 和 user:export 权限")
    @PostMapping("/check-permission-and")
    @SaCheckPermission(value = { "user:delete", "user:export" }, mode = SaMode.AND)
    public RetObj checkPermissionAnd() {
        return RetObj.success("您同时拥有 user:delete 和 user:export 权限，验证通过");
    }

    @Operation(summary = "注解示例-忽略鉴权", description = "无需登录即可访问（常用于注册、验证码等公开接口）")
    @PostMapping("/public-api")
    @SaIgnore
    public RetObj publicApi() {
        return RetObj.success("这是一个公开接口，@SaIgnore 生效");
    }

}
