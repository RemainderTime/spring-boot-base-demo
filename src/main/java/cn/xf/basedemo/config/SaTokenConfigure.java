package cn.xf.basedemo.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description: sa token拦截器注册类
 * @ClassName: SaTokenConfigure
 * @Author: xiongfeng
 * @Date: 2025/8/24 20:30
 * @Version: 1.0
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
	// 注册 Sa-Token 拦截器，打开注解式鉴权功能
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 注册 Sa-Token 拦截器，打开注解式鉴权功能
		registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
	}
}
