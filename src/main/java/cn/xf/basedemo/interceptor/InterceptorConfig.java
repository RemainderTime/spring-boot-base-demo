package cn.xf.basedemo.interceptor;

import org.springframework.context.annotation.Bean;
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

    @Bean
    public TokenInterceptor tokenInterceptor() {
        return new TokenInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor()) //登录逻辑拦截类
                .addPathPatterns("/**") //需要拦截的请求（设置的全部拦截）
                .excludePathPatterns("/user/login", "/web/**", "/pay/callback/**"); //忽略的请求
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
