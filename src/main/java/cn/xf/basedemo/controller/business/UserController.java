package cn.xf.basedemo.controller.business;

import cn.xf.basedemo.common.model.LoginUser;
import cn.xf.basedemo.common.model.RetObj;
import cn.xf.basedemo.interceptor.SessionContext;
import cn.xf.basedemo.model.res.LoginInfoRes;
import cn.xf.basedemo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: xf-boot-base
 * @ClassName UserController
 * @description: 用户控制器
 * @author: xiongfeng
 * @create: 2022-06-28 09:17
 **/
@RestController(value = "用户控制器")
@RequestMapping("/user")
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
    public RetObj info(){
        LoginUser loginUser = SessionContext.getInstance().get();
        return RetObj.success(loginUser);
    }
}
