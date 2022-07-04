package cn.xf.basedemo.common.model;

import lombok.Data;

/**
 * @program: xf-boot-base
 * @ClassName RetObj
 * @description: 全局统一响应对象
 * @author: xiongfeng
 * @create: 2022-07-04 15:57
 **/
@Data
public class RetObj<T> {

    private Integer code;

    private String message;

    private T data;

    public static <T> RetObj<T> success() {
        return new RetObj();
    }


}
