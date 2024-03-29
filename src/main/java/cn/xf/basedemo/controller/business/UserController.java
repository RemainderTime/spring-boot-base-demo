package cn.xf.basedemo.controller.business;

import cn.xf.basedemo.common.model.LoginUser;
import cn.xf.basedemo.common.model.RetObj;
import cn.xf.basedemo.interceptor.SessionContext;
import cn.xf.basedemo.model.res.LoginInfoRes;
import cn.xf.basedemo.service.UserService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
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
@Api(tags = "用户控制器")
@RestController(value = "用户控制器")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户登录", notes = "用户登录")
    @ApiOperationSupport(order = 1)
    @PostMapping("/login")
    public RetObj login(@RequestBody LoginInfoRes res){

        return userService.login(res);
    }

    @ApiOperation(value = "用户信息", notes = "用户信息")
    @ApiOperationSupport(order = 2)
    @PostMapping("/info")
    public RetObj info(){
        LoginUser loginUser = SessionContext.getInstance().get();
        return RetObj.success(loginUser);
    }
}
