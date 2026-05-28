package cn.xf.basedemo.common.exception;

import cn.xf.basedemo.common.enums.SystemStatus;
import lombok.Getter;

/**
 * @Author: xiongfeng
 * @CreateTime: 2023-11-08  13:46
 * @Description: TODO 登录异常类
 * @Version: 1.0
 */
@Getter
public class LoginException extends RuntimeException{

    private final SystemStatus status;

    public LoginException() {
        super(String.format("%s", SystemStatus.UNAUTHORIZED.getErrorMessage()));
        this.status = SystemStatus.UNAUTHORIZED;
    }

    public LoginException(Throwable e) {
        super(e);
        this.status = SystemStatus.UNAUTHORIZED;
    }

    public LoginException(String msg) {
        this(SystemStatus.UNAUTHORIZED, msg);
    }

    public LoginException(SystemStatus status) {
        super(String.format("%s", status.getErrorMessage()));
        this.status = status;
    }

    public LoginException(SystemStatus status, String msg) {
        super(msg);
        this.status = status;
    }

}
