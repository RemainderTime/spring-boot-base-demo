package cn.xf.basedemo.common.utils;

import cn.xf.basedemo.common.exception.LoginException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * packageName com.wenliao.customer.common.utils
 *
 * @author remaindertime
 * @className RequestHeaderUtil
 * @date 2024/10/10
 * @description
 */
public class RequestHeaderUtil {

    /**
     * 获取所有请求头
     * @param request HttpServletRequest 对象
     * @return 请求头的键值对 Map
     */
    public static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headersMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                headersMap.put(headerName, headerValue);
            }
        }
        return headersMap;
    }

    /**
     * 获取特定的请求头
     * @param request HttpServletRequest 对象
     * @param headerName 请求头名称
     * @return 请求头值，如果没有则返回 null
     */
    public static String getHeader(HttpServletRequest request, String headerName) {
        return request.getHeader(headerName);
    }
    /**
     * 获取token
     */
    public static String getToken(HttpServletRequest request) {
        //登录处理
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token))
            token = request.getParameter("token");
        if (StringUtils.isEmpty(token)) {
            throw new LoginException("请先登录");
        }

        // 如果 Authorization 头部中存在且以 "Bearer " 开头，则提取 token
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // 去除 "Bearer " 部分，获取真正的 token
        }
        return token;
    }
}
