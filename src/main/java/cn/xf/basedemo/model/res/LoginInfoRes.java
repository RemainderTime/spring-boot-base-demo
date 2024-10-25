package cn.xf.basedemo.model.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @program: xf-boot-base
 * @ClassName LoginInfoRes
 * @description:
 * @author: xiongfeng
 * @create: 2022-07-04 11:46
 **/
@Data
public class LoginInfoRes {

    /**
     * 登录密文
     */
    @Schema(name = "登录密文")
    private String encryptedData;
}
