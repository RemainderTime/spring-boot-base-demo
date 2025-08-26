package cn.xf.basedemo.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @program: xf-boot-base
 * @ClassName ApplicationContextUtils
 * @description:
 * @author: xiongfeng
 * @create: 2022-07-06 14:13
 **/
@Component
public class ApplicationContextUtils implements ApplicationContextAware {
    //放置在获取bean的时候提示空指针，将其定义为静态变量
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtils.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }
}
