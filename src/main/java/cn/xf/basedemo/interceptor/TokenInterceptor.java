package cn.xf.basedemo.interceptor;

import cn.xf.basedemo.common.model.LoginUserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class TokenInterceptor implements HandlerInterceptor {


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    //不拦截的请求列表
    private static final List<String> EXCLUDE_PATH_LIST = Arrays.asList("/user/login");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        if(EXCLUDE_PATH_LIST.contains(requestURI)){
            return true;
        }
        //登录处理
        String token = request.getHeader("Authorization");
        if(StringUtils.isEmpty(token))
            token = request.getParameter("token");

        String value = redisTemplate.opsForValue().get(token);
        if(StringUtils.isEmpty(value)){
            return false;
        }
        LoginUserInfo loginUserInfo = objectMapper.convertValue(value, LoginUserInfo.class);
        if(loginUserInfo == null || loginUserInfo.getId() <= 0){
            return false;
        }
        redisTemplate.expire(token, 86700, TimeUnit.SECONDS);

        //用户信息设置到上下文
        SessionContext.getInstance().set(loginUserInfo);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
