package cn.xf.basedemo.service;

import cn.xf.basedemo.common.model.RetObj;
import cn.xf.basedemo.model.req.LoginInfoReq;

/**
 * @program: xf-boot-base
 * @ClassName UserService
 * @description:
 * @author: xiongfeng
 * @create: 2022-06-28 09:21
 **/
public interface UserService {

    RetObj login(LoginInfoReq res);

    RetObj syncEs(Long userId);

    RetObj getEsId(Long userId);

    RetObj sendMQMsg(String msg);
}
