package cn.xf.basedemo.interceptor;

import cn.xf.basedemo.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor()) //登录逻辑拦截类
                .addPathPatterns("/**") //需要拦截的请求（设置的全部拦截）
                .excludePathPatterns("/user/login","/web/**"); //忽略的请求
    }
}
