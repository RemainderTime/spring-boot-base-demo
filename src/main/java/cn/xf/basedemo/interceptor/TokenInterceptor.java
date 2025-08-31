package cn.xf.basedemo.interceptor;

import ch.qos.logback.core.LayoutBase;
import cn.xf.basedemo.common.exception.LoginException;
import cn.xf.basedemo.common.exception.ResponseCode;
import cn.xf.basedemo.common.model.CustomUserDetails;
import cn.xf.basedemo.common.model.LoginUser;
import cn.xf.basedemo.common.utils.ApplicationContextUtils;
import cn.xf.basedemo.mappers.SysPermissionMapper;
import cn.xf.basedemo.mappers.SysRoleMapper;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @program: spring-boot-base-demo
 * @ClassName TokenInterceptor
 * @description: 拦截器职责（日志、请求校验、限流）
 * @author: xiongfeng
 * @create: 2022-06-16 14:17
 **/
@Component
public class TokenInterceptor implements HandlerInterceptor {

    //不拦截的请求列表
    private static final List<String> EXCLUDE_PATH_LIST = Arrays.asList("/user/login", "/web/login", "/swagger-ui.html", "/v3/api-docs", "/swagger-ui/index.html");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        if (EXCLUDE_PATH_LIST.contains(requestURI) ||
                requestURI.contains("/swagger-ui") ||
                requestURI.contains("/v3/api-docs")) {
            return true;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        SessionContext.getInstance().clear();
    }


}
