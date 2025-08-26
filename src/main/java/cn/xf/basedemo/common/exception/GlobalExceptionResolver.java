package cn.xf.basedemo.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description: 全局异常捕获类（所有异常（包括拦截器、Controller、视图））HandlerExceptionResolver更底层
 * @ClassName: GlobalExceptionResolver
 * @Author: xiongfeng
 * @Date: 2025/8/23 23:30
 * @Version: 1.0
 */
@Slf4j
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		response.setContentType("application/json;charset=UTF-8");

		try (PrintWriter writer = response.getWriter()) {
			if (ex instanceof LoginException) {
				response.setStatus(HttpStatus.FORBIDDEN.value());
				LoginException le = (LoginException) ex;
				writer.write(new ObjectMapper().writeValueAsString(
						new GenericResponse(le.getCode(), null, le.getMessage())
				));
			} else if (ex instanceof BusinessException) {
				BusinessException be = (BusinessException) ex;
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				writer.write(new ObjectMapper().writeValueAsString(
						new GenericResponse(be.getCode(), null, be.getMessage())
				));
			} else {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				writer.write(new ObjectMapper().writeValueAsString(
						new GenericResponse(500, null, "系统异常")
				));
			}
		} catch (IOException ioEx) {
			log.error("写响应失败", ioEx);
		}
		return new ModelAndView();
	}
}
