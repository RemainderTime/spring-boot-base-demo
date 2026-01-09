package cn.xf.basedemo.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @program: xf-boot-base
 * @ClassName LoginInfoRes
 * @description:
 * @author: xiongfeng
 * @create: 2022-07-04 11:46
 **/
@Data
public class LoginInfoReq {

    /**
     * 登录密文
     */
    @NotBlank(message = "登录密文不能为空")
    @Schema(name = "登录密文")
    private String encryptedData;
}
