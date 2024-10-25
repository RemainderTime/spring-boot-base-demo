package cn.xf.basedemo.interceptor;

import cn.xf.basedemo.common.exception.LoginException;
import cn.xf.basedemo.common.exception.ResponseCode;
import cn.xf.basedemo.common.model.LoginUser;
import cn.xf.basedemo.common.utils.ApplicationContextUtils;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @program: spring-boot-base-demo
 * @ClassName TokenInterceptor
 * @description:
 * @author: xiongfeng
 * @create: 2022-06-16 14:17
 **/
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;


    //不拦截的请求列表
    private static final List<String> EXCLUDE_PATH_LIST = Arrays.asList("/user/login", "/web/login");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        if (EXCLUDE_PATH_LIST.contains(requestURI)) {
            return true;
        }
        //登录处理
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token))
            token = request.getParameter("token");
        if (StringUtils.isEmpty(token)) {
            throw new LoginException("请先登录");
        }
        String value = (String) redisTemplate.opsForValue().get("token:" + token);
        if (StringUtils.isEmpty(value)) {
            throw new LoginException();
        }
        JSONObject jsonObject = JSONObject.parseObject(value);
        //JSON对象转换成Java对象
        LoginUser loginUserInfo = JSONObject.toJavaObject(jsonObject, LoginUser.class);
        if (loginUserInfo == null || loginUserInfo.getId() <= 0) {
            throw new LoginException(ResponseCode.USER_INPUT_ERROR);
        }
        redisTemplate.expire(token, 86700, TimeUnit.SECONDS);

        //用户信息设置到上下文
        SessionContext.getInstance().set(loginUserInfo);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        SessionContext.getInstance().clear();
    }
}
