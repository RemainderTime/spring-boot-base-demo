package cn.xf.basedemo.common.exception;

import cn.xf.basedemo.common.enums.SystemStatus;
import lombok.Getter;

/**
 * @Author: xiongfeng
 * @CreateTime: 2023-11-08  13:54
 * @Description: TODO 业务统一异常处理类
 * @Version: 1.0
 */
@Getter
public class BusinessException extends RuntimeException{
    private final SystemStatus status;

    public BusinessException() {
        super(String.format("%s", SystemStatus.ERROR.getErrorMessage()));
        this.status = SystemStatus.ERROR;
    }

    public BusinessException(Throwable e) {
        super(e);
        this.status = SystemStatus.ERROR;
    }

    public BusinessException(String msg) {
        this(SystemStatus.ERROR, msg);
    }

    public BusinessException(SystemStatus status) {
        super(String.format("%s", status.getErrorMessage()));
        this.status = status;
    }

    public BusinessException(SystemStatus status, String msg) {
        super(msg);
        this.status = status;
    }
}
