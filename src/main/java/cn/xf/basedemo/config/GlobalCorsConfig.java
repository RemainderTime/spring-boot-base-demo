//package cn.xf.basedemo.config;
//
//import cn.xf.basedemo.interceptor.CustomAccessDeniedHandler;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
///**
// * Description: 全局跨域配置
// *
// */
//@Slf4j
//@Configuration
//@EnableMethodSecurity(prePostEnabled = true)  // 开启 @PreAuthorize/@PostAuthorize
//public class GlobalCorsConfig {
//
//	@ConditionalOnMissingBean
//	@Bean
//	public FilterRegistrationBean<CorsFilter> corsFilter() {
//		CorsConfiguration config = new CorsConfiguration();
//		// 放行哪些原始域
//		//config.addAllowedOrigin("*");
//		// 放行哪些原始域,SpringBoot2.4.4下低版本使用.allowedOrigins("*")
//		config.addAllowedOriginPattern("*");
//		// 放行哪些原始请求头部信息
//		config.addAllowedHeader("*");
//		// 放行全部请求
//		config.addAllowedMethod("*");
//		// 是否发送Cookie
//		config.setAllowCredentials(true);
//
//		UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
//		configSource.registerCorsConfiguration("/**", config);
//
//		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(configSource));
//		// 这个顺序很重要哦，为避免麻烦请设置在最前
//		bean.setOrder(0);
//		return bean;
//	}
//
//}
