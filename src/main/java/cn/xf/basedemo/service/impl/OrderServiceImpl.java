package cn.xf.basedemo.service.impl;

import cn.xf.basedemo.common.model.RetObj;
import cn.xf.basedemo.config.pay.AliPayConfigProperties;
import cn.xf.basedemo.model.req.PayOrderFrom;
import cn.xf.basedemo.model.res.PayOrderRes;
import cn.xf.basedemo.service.OrderService;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * OrderServiceImpl
 *
 * @author 海言
 * @date 2025/10/22
 * @time 14:08
 * @Description
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private AlipayClient alipayClient;
    @Resource
    private AliPayConfigProperties aliPayConfigProperties;

    @Override
    public RetObj<PayOrderRes> aliCreateOrder(PayOrderFrom from) {

        try {
            //校验用户数据
            //校验商品库存
            //校验余额账户
            //...
            //创建订单

            //获取订单编码
            String orderNo = "O" + System.currentTimeMillis();
            // PC/H5方式支付
            AlipayTradePagePayRequest request = getAlipayTradePagePayRequest(from, orderNo);
            AlipayTradePagePayResponse payResponse = alipayClient.pageExecute(request);
            //创建支付单数据
            //...
            String body = payResponse.getBody();
            PayOrderRes res = new PayOrderRes();
            res.setOrderNo(orderNo);
            res.setFrom(body);
            return RetObj.success(res);
        } catch (Exception e) {
            log.error("创建订单失败", e);
        }
        return null;
    }

    @Override
    public RetObj<PayOrderRes> aliAppCreateOrder(PayOrderFrom from) {
        //校验
        //...
        String orderNo = "O" + System.currentTimeMillis();
        AlipayTradeAppPayRequest request = getAlipayTradeAppPayRequest(from, orderNo);
        try {
            String body = alipayClient.sdkExecute(request).getBody();
            PayOrderRes res = new PayOrderRes();
            res.setOrderNo(orderNo);
            //返回给前端调起本机支付宝APP支付
            res.setFrom(body);
            //创建订单
            //...
            return RetObj.success(res);
        }catch (Exception e){
            log.error("APP创建订单失败", e);
        }
        return null;
    }

    @Override
    public String aliCallback(HttpServletRequest request) {
        try {
            log.info("支付回调，消息接收 {}", request.getParameter("trade_status"));
            if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
                Map<String, String> params = new HashMap<>();
                Map<String, String[]> requestParams = request.getParameterMap();
                for (String name : requestParams.keySet()) {
                    params.put(name, request.getParameter(name));
                }
                String tradeNo = params.get("out_trade_no");
                String gmtPayment = params.get("gmt_payment");
                String alipayTradeNo = params.get("trade_no");
                String sign = params.get("sign");
                String content = AlipaySignature.getSignCheckContentV1(params);
                boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, aliPayConfigProperties.getAlipayPublicKey(), "UTF-8"); // 验证签名
                // 支付宝验签
                if (checkSignature) {
                    // 验签通过
                    log.info("支付回调，交易名称: {}", params.get("subject"));
                    log.info("支付回调，交易状态: {}", params.get("trade_status"));
                    log.info("支付回调，支付宝交易凭证号: {}", params.get("trade_no"));
                    log.info("支付回调，商户订单号: {}", params.get("out_trade_no"));
                    log.info("支付回调，交易金额: {}", params.get("total_amount"));
                    log.info("支付回调，买家在支付宝唯一id: {}", params.get("buyer_id"));
                    log.info("支付回调，买家付款时间: {}", params.get("gmt_payment"));
                    log.info("支付回调，买家付款金额: {}", params.get("buyer_pay_amount"));
                    log.info("支付回调，支付回调，更新订单 {}", tradeNo);

                    // 更新订单未已支付
//                    orderService.changeOrderPaySuccess(tradeNo);
                }
            }
            return "success";
        } catch (Exception e) {
            log.error("支付回调，处理失败", e);
            return "false";
        }
    }

    /**
     * 回查订单支付状态
     *
     * @param orderNo
     * @return
     */
    @Override
    public String queryAlipayOrderStatus(String orderNo) {
        //实践方案：创建定时任务，查询订单数据中超过某个自定义时间未支付的订单进行回查
        try {
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            AlipayTradeQueryModel bizModel = new AlipayTradeQueryModel();
            bizModel.setOutTradeNo(orderNo);
            request.setBizModel(bizModel);
            AlipayTradeQueryResponse alipayTradeQueryResponse = alipayClient.execute(request);
            String code = alipayTradeQueryResponse.getCode();
            // 判断状态码
            if ("10000".equals(code)) {
                //更新订单状态
                //...
                return "success";
            }
        }catch (Exception e){
            log.error("回查支付订单失败", e);
        }
        return "not";
    }

    @NotNull
    private AlipayTradePagePayRequest getAlipayTradePagePayRequest(PayOrderFrom from, String orderNo) {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl("http://zfd8b38d.natappfree.cc/pay/callback/ali/notice");
        request.setReturnUrl("www.baidu.com");
        //构建请求参数
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderNo);
        bizContent.put("total_amount", from.getPrice().multiply(new BigDecimal(from.getNum())));
        bizContent.put("subject", from.getProductName());
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toJSONString());
        return request;
    }

    @NotNull
    private AlipayTradeAppPayRequest getAlipayTradeAppPayRequest(PayOrderFrom from, String orderNo) {
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setNotifyUrl(aliPayConfigProperties.getNotifyUrl());
        request.setReturnUrl(aliPayConfigProperties.getReturnUrl());
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setOutTradeNo(orderNo);
        model.setTotalAmount(from.getPrice().multiply(new BigDecimal(from.getNum())).toString());
        model.setSubject(from.getProductName());
        model.setProductCode("APP_INSTANT_TRADE_PAY");
        model.setBody(from.getProductName());
        request.setBizModel(model);
        return request;
    }
}
