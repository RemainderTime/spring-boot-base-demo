package cn.xf.basedemo.controller.business;


import cn.xf.basedemo.common.model.RetObj;
import cn.xf.basedemo.model.req.PayOrderFrom;
import cn.xf.basedemo.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * PayOrderController
 *
 * @author 海言
 * @date 2025/10/22
 * @time 14:07
 * @Description 支付下单控制器
 */
@RestController
@RequestMapping("/pay")
public class PayOrderController {

    @Resource
    private OrderService orderService;

    @Operation(summary = "支付宝PC/H5支付下单", description = "支付宝PC/H5支付下单")
    @PostMapping("/ali/createOrder")
    public RetObj aliCreateOrder(@RequestBody PayOrderFrom from) {

        return orderService.aliCreateOrder(from);
    }

    @Operation(summary = "支付宝app支付下单", description = "支付宝app支付下单")
    @PostMapping("/ali/app/createOrder")
    public RetObj aliAppCreateOrder(@RequestBody PayOrderFrom from) {

        return orderService.aliAppCreateOrder(from);
    }

    //掉单查询支付宝支付订单状态
    @Operation(summary = "掉单查询支付宝支付订单状态", description = "掉单查询支付宝支付订单状态")
    @GetMapping("/ali/queryOrderStatus")
    public String queryAlipayOrderStatus(String orderNo) {
        return orderService.queryAlipayOrderStatus(orderNo);
    }


}

