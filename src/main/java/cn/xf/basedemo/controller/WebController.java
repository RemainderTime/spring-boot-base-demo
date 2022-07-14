package cn.xf.basedemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: xf-boot-base
 * @ClassName LoginController
 * @description: 页面跳转转发控制器
 * @author: xiongfeng
 * @create: 2022-07-06 11:51
 **/
@Controller
@RequestMapping("web")
public class WebController {

    /**
     * @Description 进入登录页面
     * @Author xiongfeng
     * @Date 2022/7/6
     * @Param []
     * @return java.lang.String
     **/
    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    /**
     * @Description 静态页面跳转统一入口
     * @Author xiongfeng
     * @Date 2022/7/6
     * @Param [url]
     * @return java.lang.String
     **/
    @RequestMapping(value = "/{url}")
    public String skipUrl(@PathVariable(name = "url") String url) {
        return url;
    }

}
