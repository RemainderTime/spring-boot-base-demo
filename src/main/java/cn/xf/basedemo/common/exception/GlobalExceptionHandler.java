package cn.xf.basedemo.common.exception;

import cn.xf.basedemo.common.enums.SystemStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xiongfeng
 * @CreateTime: 2023-11-08  11:48
 * @Description: TODO 全局异常捕获类（仅 Controller 层异常）
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
    public GenericResponse tokenExceptionHandler(HttpServletRequest request, final LoginException e, HttpServletResponse response) {
        log.error("token exception", e);
        LoginException exception = (LoginException) e;
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return new GenericResponse(exception.getCode(),null,exception.getMessage());
    }

    /**
     * 重写handleMethodArgumentNotValid 方法自定义处理参数校验错误信息
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * 访问权限不足异常捕获
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleAccessDenied(AccessDeniedException e) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 403);
        result.put("msg", "权限不足，请联系管理员");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
    }

    /**
     * 其他异常捕获
     * @param request
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionHandler(jakarta.servlet.http.HttpServletRequest request, final Exception e, jakarta.servlet.http.HttpServletResponse response) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", e.getMessage());
        log.error("error------{}", e);
        return ResponseEntity.status(SystemStatus.ERROR.getCode()).body(errors);
    }

}
