package cn.xf.basedemo.service.impl;

import cn.xf.basedemo.common.model.ChatGPTProperties;
import cn.xf.basedemo.common.model.LoginInfo;
import cn.xf.basedemo.common.model.LoginUser;
import cn.xf.basedemo.common.model.RetObj;
import cn.xf.basedemo.common.utils.JwtTokenUtils;
import cn.xf.basedemo.common.utils.OpenAIUtils;
import cn.xf.basedemo.common.utils.RSAUtils;
import cn.xf.basedemo.config.GlobalConfig;
import cn.xf.basedemo.mappers.MessageMapper;
import cn.xf.basedemo.mappers.UserMapper;
import cn.xf.basedemo.model.domain.Message;
import cn.xf.basedemo.model.domain.User;
import cn.xf.basedemo.model.res.LoginInfoRes;
import cn.xf.basedemo.service.UserService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.security.auth.login.LoginContext;
import java.util.HashMap;
import java.util.Map;
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
    private MessageMapper messageMapper;

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
    public RetObj sendChat(String content, String name) {
        if(StringUtils.isEmpty(content)){
            return RetObj.error("内容不能为空");
        }
        Message message =new Message();
        message.setName(name);
        message.setContent(content);
        messageMapper.insert(message);

        OpenAiService openAiService = new OpenAiService(globalConfig.getOpenAIKey());
        ChatGPTProperties chatGPTProperties = new ChatGPTProperties();
        chatGPTProperties.setModel(globalConfig.getOpenAIModel());
        chatGPTProperties.setToken(globalConfig.getRsaPublicKey());
        chatGPTProperties.setRetries(Integer.valueOf(globalConfig.getOpenAIRetries()));
        String answer = OpenAIUtils.getAnswer(openAiService, chatGPTProperties, content);
        return RetObj.success(answer);
    }
}
