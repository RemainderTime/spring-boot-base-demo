package cn.xf.basedemo.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import cn.xf.basedemo.common.enums.SystemStatus;
import cn.xf.basedemo.common.model.RetObj;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Description: spring security体系 全局跨域配置
 */
@Slf4j
@Configuration
@EnableMethodSecurity(prePostEnabled = true)  // 开启 @PreAuthorize/@PostAuthorize
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   TokenAuthenticationFilter tokenAuthenticationFilter) throws Exception {
        http
                .cors(Customizer.withDefaults())  // 开启 CORS
                .csrf(AbstractHttpConfigurer::disable) //前后端分离 禁用csrf
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/login", "/web/login").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/doc.html", "/webjars/**", "/swagger-resources/**").permitAll()
                        .anyRequest().authenticated()
                )  // 把自定义过滤器插入 Spring Security 过滤器链
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            RetObj<Void> retObj = new RetObj<>(SystemStatus.UNAUTHORIZED.getCode(), "未登录或Token失效");
                            response.getWriter().write(JSONObject.toJSONString(retObj));
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json;charset=UTF-8");
                            RetObj<Void> retObj = new RetObj<>(SystemStatus.FORBIDDEN.getCode(), "没有权限访问该资源");
                            response.getWriter().write(JSONObject.toJSONString(retObj));
                        })
                );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
