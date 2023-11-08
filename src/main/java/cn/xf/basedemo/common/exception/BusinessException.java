package cn.xf.basedemo.common.exception;

import lombok.Getter;

/**
 * @Author: xiongfeng
 * @CreateTime: 2023-11-08  13:54
 * @Description: TODO 业务统一异常处理类
 * @Version: 1.0
 */
@Getter
public class BusinessException extends RuntimeException{
    private final ResponseCode code;

    public BusinessException() {
        super(String.format("%s", ResponseCode.INTERNAL_ERROR.getMessage()));
        this.code = ResponseCode.INTERNAL_ERROR;
    }

    public BusinessException(Throwable e) {
        super(e);
        this.code = ResponseCode.INTERNAL_ERROR;
    }

    public BusinessException(String msg) {
        this(ResponseCode.INTERNAL_ERROR, msg);
    }

    public BusinessException(ResponseCode code) {
        super(String.format("%s", code.getMessage()));
        this.code = code;
    }

    public BusinessException(ResponseCode code, String msg) {
        super(msg);
        this.code = code;
    }
}
