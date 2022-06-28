package cn.xf.basedemo.service.impl;

import cn.xf.basedemo.common.utils.RSAUtils;
import cn.xf.basedemo.config.GlobalConfig;
import cn.xf.basedemo.service.UserService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: xf-boot-base
 * @ClassName UserServiceImpl
 * @description:
 * @author: xiongfeng
 * @create: 2022-06-28 09:21
 **/
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private GlobalConfig globalConfig;

    @Override
    public String login(String encryptedData) {

        if(StringUtils.isEmpty(encryptedData)){
            return null;
        }

        String loginJson = "";
        try {
            loginJson = RSAUtils.privateDecryption(encryptedData, RSAUtils.getPrivateKey(globalConfig.getRsaPrivateKey()));
        }catch (Exception e){
            log.error("解密失败------》》》");
        }
        log.error("解密数据----" + loginJson);




        return null;
    }
}
