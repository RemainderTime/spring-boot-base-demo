package cn.xf.basedemo.service.impl;

import cn.xf.basedemo.common.model.EsBaseModel;
import cn.xf.basedemo.common.model.LoginInfo;
import cn.xf.basedemo.common.model.LoginUser;
import cn.xf.basedemo.common.model.RetObj;
import cn.xf.basedemo.common.utils.EsUtil;
import cn.xf.basedemo.common.utils.JwtTokenUtils;
import cn.xf.basedemo.common.utils.RSAUtils;
import cn.xf.basedemo.common.utils.StringUtil;
import cn.xf.basedemo.config.GlobalConfig;
import cn.xf.basedemo.mappers.UserMapper;
import cn.xf.basedemo.model.domain.User;
import cn.xf.basedemo.model.res.LoginInfoRes;
import cn.xf.basedemo.mq.RocketMqMsgProducer;
import cn.xf.basedemo.service.UserService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public RetObj login(LoginInfoRes res) {

        if (Objects.isNull(res) || StringUtils.isEmpty(res.getEncryptedData())) {
            return null;
        }
        String loginJson = "";
        try {
            loginJson = RSAUtils.privateDecryption(res.getEncryptedData(), RSAUtils.getPrivateKey(globalConfig.getRsaPrivateKey()));
        } catch (Exception e) {
            log.error("解密失败------", e);
        }
        LoginInfo loginInfo = null;
        try {
            loginInfo = objectMapper.readValue(loginJson, LoginInfo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return RetObj.error("账号或密码错误");
        }
        if (!StringUtils.isEmpty(loginInfo.check())) {
            return RetObj.error(loginInfo.check());
        }
        //校验登录账号密码
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account", loginInfo.getAccount());
        queryWrapper.eq("password", loginInfo.getPwd());
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(user)) {
            return RetObj.error("账号或密码错误");
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setId(user.getId());
        loginUser.setAccount(user.getAccount());
        loginUser.setName(user.getName());
        loginUser.setPhone(user.getPhone());

        String token = JwtTokenUtils.createToken(user.getId());
        loginUser.setToken(token);

        redisTemplate.opsForValue().set("token:" + token, JSONObject.toJSONString(loginUser), 3600, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set("user_login_token:" + user.getId(), token, 3600, TimeUnit.SECONDS);

        return RetObj.success(loginUser);
    }

    @Override
    public RetObj syncEs(Long userId) {
        User user = userMapper.selectById(userId);
        if (Objects.isNull(user)) {
            return RetObj.error("用户不存在");
        }
        String index = StringUtil.camelToKebabCase(user.getClass().getSimpleName());
        if (!EsUtil.existIndex(index)) {
            EsUtil.createIndex(index);
        }
        EsUtil.addDocument(new EsBaseModel(index, String.valueOf(user.getId()), user, user.getClass()));
        return RetObj.success();
    }

    @Override
    public RetObj getEsId(Long userId) {
        Object user = EsUtil.getDocumentById(new EsBaseModel("user", String.valueOf(userId), null, User.class));
        if (Objects.nonNull(user)) {
            return RetObj.success(user);
        }
        return RetObj.error("es中不存在该用户");
    }
}
