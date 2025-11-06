package cn.xf.basedemo.controller.business;

import cn.xf.basedemo.common.model.LoginUser;
import cn.xf.basedemo.common.model.RetObj;
import cn.xf.basedemo.interceptor.SessionContext;
import cn.xf.basedemo.model.res.LoginInfoRes;
import cn.xf.basedemo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
    public RetObj login(@RequestBody LoginInfoRes res) {

        return userService.login(res);
    }

    @Operation(summary = "用户信息", description = "用户信息")
    @PostMapping("/info")
    public RetObj info() {
        LoginUser loginUser = SessionContext.getInstance().get();
        return RetObj.success(loginUser);
    }

    @Operation(summary = "es同步用户信息", description = "用户信息")
    @GetMapping("/syncEs")
    public RetObj syncEs(Long userId) {
        return userService.syncEs(userId);
    }

    @Operation(summary = "es查询用户信息", description = "用户信息")
    @GetMapping("/getEsId")
    public RetObj getEsId(Long userId) {
        return userService.getEsId(userId);
    }

}
