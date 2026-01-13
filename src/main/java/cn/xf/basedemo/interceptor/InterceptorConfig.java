package cn.xf.basedemo.interceptor;

import cn.dev33.satoken.interceptor.SaInterceptor;
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

    @org.springframework.beans.factory.annotation.Autowired
    private SaTokenContextInterceptor saTokenContextInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，定义详细认证规则
        registry.addInterceptor(new SaInterceptor(handler -> {
            // 指定一条 match 规则
            cn.dev33.satoken.stp.StpUtil.checkLogin();
        }))
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/web/**", "/swagger-resources/**", "/webjars/**", "/v3/**",
                        "/doc.html");

        // 注册 Context 拦截器，用于注入 SessionContext
        registry.addInterceptor(saTokenContextInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/web/**", "/swagger-resources/**", "/webjars/**", "/v3/**",
                        "/doc.html");
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
