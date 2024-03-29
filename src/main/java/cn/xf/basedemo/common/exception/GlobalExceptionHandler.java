package cn.xf.basedemo.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: xiongfeng
 * @CreateTime: 2023-11-08  11:48
 * @Description: TODO 全局异常捕获类
 * @Version: 1.0
 */
@Slf4j
@RestControllerAdvice
@Component
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    /**
     * 定义要捕获的异常 可以多个 @ExceptionHandler({})     *
     * @param request  request
     * @param e        exception
     * @param response response
     * @return 响应结果
     */
    @ExceptionHandler(BusinessException.class)
    public GenericResponse customExceptionHandler(HttpServletRequest request, final Exception e, HttpServletResponse response) {
        BusinessException exception = (BusinessException) e;

        if (exception.getCode() == ResponseCode.USER_INPUT_ERROR) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        } else if (exception.getCode() == ResponseCode.FORBIDDEN) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return new GenericResponse(exception.getCode(), null, exception.getMessage());
    }

    /**
     * 登录异常捕获
     * @param request
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(LoginException.class)
    public GenericResponse tokenExceptionHandler(HttpServletRequest request, final Exception e, HttpServletResponse response) {
        log.error("token exception", e);
        LoginException exception = (LoginException) e;
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return new GenericResponse(exception.getCode(),null,exception.getMessage());
    }

}
