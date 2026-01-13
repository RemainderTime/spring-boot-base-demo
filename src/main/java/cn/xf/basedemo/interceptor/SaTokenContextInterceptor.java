package cn.xf.basedemo.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import cn.xf.basedemo.common.model.LoginUser;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @Description: Sa-Token 上下文拦截器
 *               用于将 Sa-Token Session 中的用户信息注入到 SessionContext (ThreadLocal)
 *               以兼容旧的业务代码 (SessionContext.getInstance().get())
 * @Author: xiongfeng
 */
@Component
public class SaTokenContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 如果已登录，尝试从 Session 中获取用户信息并注入 ThreadLocal
        if (StpUtil.isLogin()) {
            // 从 Sa-Token Session 中读取 loginUser (需确保登录时已存入)
            LoginUser loginUser = (LoginUser) StpUtil.getSession().get("loginUser");
            if (loginUser != null) {
                SessionContext.getInstance().set(loginUser);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // 请求结束后清理 ThreadLocal，防止内存泄漏
        SessionContext.getInstance().clear();
    }
}
