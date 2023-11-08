package cn.xf.basedemo.common.exception;

import lombok.Data;

/**
 * @Author: xiongfeng
 * @CreateTime: 2023-11-08  13:47
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class GenericResponse<T> {
    private int code;

    private T data;

    private String message;

    public GenericResponse() {};

    public GenericResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public GenericResponse(int code, T data, String message) {
        this(code, data);
        this.message = message;
    }

    public GenericResponse(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.data = null;
        this.message = responseCode.getMessage();
    }

    public GenericResponse(ResponseCode responseCode, T data) {
        this(responseCode);
        this.data = data;
    }

    public GenericResponse(ResponseCode responseCode, T data, String message) {
        this(responseCode, data);
        this.message = message;
    }

}
