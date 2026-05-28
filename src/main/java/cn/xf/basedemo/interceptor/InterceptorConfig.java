package cn.xf.basedemo.interceptor;

import cn.dev33.satoken.interceptor.SaInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: spring-boot-base-demo
 * @ClassName InterceptorConfig
 * @description:
 * @author: xiongfeng
 * @create: 2022-06-16 13:59
 **/
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private SaTokenContextInterceptor saTokenContextInterceptor;

    // 统一配置排除路径，避免重复书写
    private static final String[] EXCLUDE_PATHS = {
            "/user/login",
            "/web/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/v3/**",
            "/doc.html",
            "/swagger-ui.html",
            "/swagger-ui/**"
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. 注册 Sa-Token 拦截器 (负责鉴权)
        registry.addInterceptor(new SaInterceptor(handler -> {
            cn.dev33.satoken.stp.StpUtil.checkLogin();
        }))
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATHS);

        // 2. 注册 Context 拦截器 (负责注入ThreadLocal，兼容旧代码)
        registry.addInterceptor(saTokenContextInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATHS);
    }

    /**
     * 放行Knife4j请求
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

    }
}
