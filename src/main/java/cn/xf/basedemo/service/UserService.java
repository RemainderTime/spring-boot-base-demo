package cn.xf.basedemo.service;

/**
 * @program: xf-boot-base
 * @ClassName UserService
 * @description:
 * @author: xiongfeng
 * @create: 2022-06-28 09:21
 **/
public interface UserService {

    String login(String encryptedData);
}
