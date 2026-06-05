package cn.xf.basedemo.common.enums;

import lombok.Getter;

@Getter
public enum SystemStatus {

    SUCCESS(200, "请求成功"),
    USER_INPUT_ERROR(400, "参数或用户输入错误"),
    UNAUTHORIZED(401, "token无效"),
    FORBIDDEN(403, "禁止访问"),
    TOO_FREQUENT_VISIT(429, "访问太频繁，请休息一会儿"),
    ERROR(500, "系统异常")
    ;


    private Integer code;

    private String errorMessage;

    SystemStatus(Integer code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }
}
