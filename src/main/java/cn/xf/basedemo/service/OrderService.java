package cn.xf.basedemo.service;

import cn.xf.basedemo.common.model.RetObj;
import cn.xf.basedemo.model.req.PayOrderFrom;

import javax.servlet.http.HttpServletRequest;

public interface OrderService {

    RetObj aliCreateOrder(PayOrderFrom from);

    RetObj aliAppCreateOrder(PayOrderFrom from);

    String aliCallback(HttpServletRequest request);

    String queryAlipayOrderStatus(String orderNo);
}
