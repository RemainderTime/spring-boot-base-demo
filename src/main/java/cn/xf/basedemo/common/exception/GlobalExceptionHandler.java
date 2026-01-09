package cn.xf.basedemo.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @Description: 全局异常处理类 (使用 @RestControllerAdvice 替代旧版
 *               HandlerExceptionResolver)
 * @Author: xiongfeng
 * @Date: 2025/1/9
 * @Version: 2.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理登录/认证异常
     */
    @ExceptionHandler(LoginException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public GenericResponse<Void> handleLoginException(LoginException e, HttpServletRequest request) {
        log.warn("认证失败 [URL:{}]: code={}, message={}", request.getRequestURI(), e.getCode(), e.getMessage());
        return new GenericResponse<>(e.getCode(), null, e.getMessage());
    }

    /**
     * 处理业务逻辑异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 或者使用 HttpStatus.OK，根据前端约定
    public GenericResponse<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常 [URL:{}]: code={}, message={}", request.getRequestURI(), e.getCode(), e.getMessage());
        return new GenericResponse<>(e.getCode(), null, e.getMessage());
    }

    /**
     * 处理参数校验异常 (处理 @Valid / @Validated 触发的异常)
     */
    @ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericResponse<Void> handleValidationException(Exception e, HttpServletRequest request) {
        BindingResult bindingResult = null;
        if (e instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
        } else if (e instanceof BindException) {
            bindingResult = ((BindException) e).getBindingResult();
        }

        String errorMsg = "参数校验失败";
        if (bindingResult != null && bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                errorMsg = fieldError.getDefaultMessage();
            }
        }
        log.warn("参数校验失败 [URL:{}]: {}", request.getRequestURI(), errorMsg);
        return new GenericResponse<>(ResponseCode.USER_INPUT_ERROR.getCode(), null, errorMsg);
    }

    /**
     * 处理所有未知的系统异常 (兜底)
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public GenericResponse<Void> handleSystemException(Exception e, HttpServletRequest request) {
        // 生产级关键点：必须记录异常堆栈，否则无法排查 BUG
        log.error("系统发生未知异常 [URL:{}]", request.getRequestURI(), e);
        return new GenericResponse<>(500, null, "系统内部繁忙，请稍后再试");
    }
}
