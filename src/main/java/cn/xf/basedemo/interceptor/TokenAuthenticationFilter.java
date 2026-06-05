package cn.xf.basedemo.interceptor;

import cn.xf.basedemo.common.enums.SystemStatus;
import cn.xf.basedemo.common.exception.LoginException;
import cn.xf.basedemo.common.model.CustomUserDetails;
import cn.xf.basedemo.common.model.LoginUser;
import cn.xf.basedemo.common.model.RetObj;
import cn.xf.basedemo.common.utils.ApplicationContextUtils;
import cn.xf.basedemo.common.utils.RequestHeaderUtil;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description:  登录权限校验过滤器（过滤器职责：登录认证和权限恢复）
 * @ClassName: TokenAuthenticationFilter
 * @Author: xiongfeng
 * @Date: 2025/8/28 22:41
 * @Version: 1.0
 */
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

	//不拦截的请求列表
	private static final List<String> EXCLUDE_PATH_LIST = Arrays.asList("/user/login", "/web/login", "/swagger-ui.html", "/v3/api-docs", "/swagger-ui/index.html");

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		//登录处理
		try {
			String requestURI = request.getRequestURI();
			if (EXCLUDE_PATH_LIST.contains(requestURI) ||
					requestURI.contains("/swagger-ui") ||
					requestURI.contains("/v3/api-docs")) {
				filterChain.doFilter(request, response);
				return;
			}
			String token = RequestHeaderUtil.getToken(request);
			String value = (String) redisTemplate.opsForValue().get("token:" + token);
			if (StringUtils.isEmpty(value)) {
				throw new LoginException();
			}
			JSONObject jsonObject = JSONObject.parseObject(value);
			//JSON对象转换成Java对象
			LoginUser loginUserInfo = JSONObject.toJavaObject(jsonObject, LoginUser.class);
			if (loginUserInfo == null || loginUserInfo.getId() <= 0) {
				throw new LoginException(SystemStatus.USER_INPUT_ERROR);
			}
			redisTemplate.expire("token:" + token, 86700, TimeUnit.SECONDS);
			//用户信息设置到上下文(如果使用Spring security 也可设置登录用户上下文数据，下面就可不用自定义设置)
			SessionContext.getInstance().set(loginUserInfo);
			//设置用户权限角色
			this.setSpringSecurityContext(loginUserInfo);
			filterChain.doFilter(request, response);
		}catch (LoginException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(JSONObject.toJSONString(new RetObj<>(e.getStatus().getCode(), e.getMessage())));
		}finally {
            // 无论请求是否异常，最后一定清理，避免 ThreadLocal 泄漏
            SessionContext.getInstance().clear();
            SecurityContextHolder.clearContext();
        }
	}
	//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
//    Long userId = user.getUserId(); // 拿到登录用户 ID

	private void setSpringSecurityContext(LoginUser loginUserInfo) {
		//直接从缓存中获取登录用户权限和角色数据
		List<String> permissionList = loginUserInfo.getPermissions();
		List<String> roleList = loginUserInfo.getRoles();
		List<String> authoritiesList = new java.util.ArrayList<>();
		if (!CollectionUtils.isEmpty(permissionList)) {
			authoritiesList.addAll(permissionList);
		}
		if (!CollectionUtils.isEmpty(roleList)) {
			//为角色拼接前缀
			List<String> roleAuthorities = roleList.stream().map(role -> "ROLE_" + role).collect(Collectors.toList());
			authoritiesList.addAll(roleAuthorities);
		}
		//封装用户权限角色
		List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(authoritiesList);
		//设置用户信息到SpringSecurity上下文
		UserDetails userDetails = new CustomUserDetails(loginUserInfo.getId(), loginUserInfo.getPhone(), authorities);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
