package cn.xf.basedemo.common.model;

import lombok.Data;

/**
 * @program: xf-boot-base
 * @ClassName LoginUserInfo
 * @description:
 * @author: xiongfeng
 * @create: 2022-06-17 14:54
 **/
@Data
public class LoginUser {

    private Integer id;

    private String name;

    private String account;

    private String phone;

    private String token;
}
