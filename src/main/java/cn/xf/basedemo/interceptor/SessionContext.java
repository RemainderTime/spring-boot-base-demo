package cn.xf.basedemo.interceptor;

import cn.xf.basedemo.common.model.LoginUser;

/**
 * @program: xf-boot-base
 * @ClassName SessionContext
 * @description:
 * @author: xiongfeng
 * @create: 2022-06-17 15:03
 **/
public class SessionContext {

    private ThreadLocal<LoginUser> threadLocal;

    private SessionContext() {
        this.threadLocal = new ThreadLocal<>();
    }

    /**
     * 使用静态内部类创建单例
     */
    private static class Context {
        private static final SessionContext INSTANCE = new SessionContext();
    }

    public static SessionContext getInstance() {
        return Context.INSTANCE;
    }

    public void set(LoginUser user) {
        this.threadLocal.set(user);
    }

    public LoginUser get() {
        return this.threadLocal.get();
    }

    public void clear() {
        this.threadLocal.remove();
    }

}
