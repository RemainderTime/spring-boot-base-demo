package cn.xf.basedemo.model.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: xf-boot-base
 * @ClassName LoginInfoRes
 * @description:
 * @author: xiongfeng
 * @create: 2022-07-04 11:46
 **/
@Data
@ApiModel(value = "登录请求对象")
public class LoginInfoRes {

    /**
     * 登录密文
     */
    @ApiModelProperty(value = "encryptedData", name = "登录密文")
    private String encryptedData;
}
