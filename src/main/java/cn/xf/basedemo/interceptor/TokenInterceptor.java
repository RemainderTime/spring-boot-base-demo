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
 * @description:
 * @author: xiongfeng
 * @create: 2022-06-16 14:17
 **/
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    private SysPermissionMapper sysPermissionMapper = ApplicationContextUtils.getBean(SysPermissionMapper.class);
    private SysRoleMapper sysRoleMapper = ApplicationContextUtils.getBean(SysRoleMapper.class);


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
        //设置用户权限角色
        this.setSpringSecurityContext(loginUserInfo);
        //用户信息设置到上下文(如果使用Spring security 也可设置登录用户上下文数据，下面就可不用自定义设置)
        SessionContext.getInstance().set(loginUserInfo);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
//    Long userId = user.getUserId(); // 拿到登录用户 ID

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        SessionContext.getInstance().clear();
    }

    /**
     * 设置用户权限角色 （Spring Security 本身的 SecurityContext 是请求级别的，每次请求都会被清理，所以每次请求都会查询权限数据并设置，
     * 安全但是很慢，所以可以做一些优化，比如把权限数据放到redis中获取和用户信息一起放在jwt中，然后登录时解析在设置到Spring security上下文中）
     * @param loginUserInfo
     */
    private void setSpringSecurityContext(LoginUser loginUserInfo) {
        //获取登录用户权限数据
        List<String> permissionList = sysPermissionMapper.getPermissionListByRoleId(loginUserInfo.getId());
        //获取用户角色数据
        List<String> roleList = sysRoleMapper.getRoleListByUserId(loginUserInfo.getId());
        if (!CollectionUtils.isEmpty(roleList)) {
            //为角色拼接前缀
            roleList = roleList.stream().map(role -> "ROLE_" + role).collect(Collectors.toList());
        }
        permissionList.addAll(roleList);
        //封装用户权限角色
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(permissionList);
        //设置用户信息到SpringSecurity上下文
        UserDetails userDetails = new CustomUserDetails(loginUserInfo.getId(), loginUserInfo.getPhone(), authorities);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
