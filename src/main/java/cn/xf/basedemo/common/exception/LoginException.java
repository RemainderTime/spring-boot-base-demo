package cn.xf.basedemo.common.exception;

import lombok.Getter;

/**
 * @Author: xiongfeng
 * @CreateTime: 2023-11-08  13:46
 * @Description: TODO 登录异常类
 * @Version: 1.0
 */
@Getter
public class LoginException extends RuntimeException{

    private final ResponseCode code;

    public LoginException() {
        super(String.format("%s", ResponseCode.AUTHENTICATION_NEEDED.getMessage()));
        this.code = ResponseCode.AUTHENTICATION_NEEDED;
    }

    public LoginException(Throwable e) {
        super(e);
        this.code = ResponseCode.AUTHENTICATION_NEEDED;
    }

    public LoginException(String msg) {
        this(ResponseCode.AUTHENTICATION_NEEDED, msg);
    }

    public LoginException(ResponseCode code) {
        super(String.format("%s", code.getMessage()));
        this.code = code;
    }

    public LoginException(ResponseCode code, String msg) {
        super(msg);
        this.code = code;
    }

}
