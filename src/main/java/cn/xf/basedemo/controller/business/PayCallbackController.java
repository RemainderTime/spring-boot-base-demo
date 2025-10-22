package cn.xf.basedemo.controller.business;


import cn.xf.basedemo.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * PayCallbackController
 *
 * @author 海言
 * @date 2025/10/22
 * @time 16:16
 * @Description 支付回调控制器
 */
@RestController
@RequestMapping("/pay/callback")
public class PayCallbackController {

    @Resource
    private OrderService userService;

    //回调
    @Operation(summary = "支付宝支付回调", description = "支付宝支付回调")
    @PostMapping("/ali/notice")
    public String aliCallback(HttpServletRequest request) {
        userService.aliCallback(request);
        return "success";
    }
}
