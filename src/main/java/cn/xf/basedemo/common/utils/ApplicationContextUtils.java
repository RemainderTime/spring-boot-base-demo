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
    private static ApplicationContext context;

    //类初始化完成之后调用setApplicationContext()方法进行操作
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtils.context = applicationContext;
    }

    public static ApplicationContext getContext(){
        return context;
    }

    public static Object getBean(String beanName){
        //在这一步的时候一定要注意，此时可调用这个方法的时候
        //context可能为空，会提示空指针异常，需要将其定义成静态的，这样类加载的时候
        //context就已经存在了
        return context.getBean(beanName);
    }
}
