package cn.xf.basedemo.common.exception;

import lombok.Getter;

/**
 * @Author: xiongfeng
 * @CreateTime: 2023-11-08  13:41
 * @Description: TODO 异常状态码
 * @Version: 1.0
 */
@Getter
public enum ResponseCode{

    SUCCESS(0, "Success"),

    INTERNAL_ERROR(1, "服务器内部错误"),

    USER_INPUT_ERROR(2, "用户输入错误"),

    AUTHENTICATION_NEEDED(3, "Token过期或无效"),

    FORBIDDEN(4, "禁止访问"),

    TOO_FREQUENT_VISIT(5, "访问太频繁，请休息一会儿");

    private final int code;

    private final String message;


    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
