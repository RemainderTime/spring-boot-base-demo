package cn.xf.basedemo.config.pay;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * AliPayConfig
 *
 * @author 海言
 * @date 2025/10/22
 * @time 13:35
 * @Description 支付宝支付配置常量
 */
@Data
@ConfigurationProperties(prefix = "pay.ali")
public class AliPayConfigProperties {

    /**
     * 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
     */
    private String appId;

    /**
     * 商户PID
     */
    private String pId;

    /**
     * 商家私钥
     */
    private String merchantPrivateKey;

    /**
     * 支付宝公钥
     */
    private String alipayPublicKey;

    /**
     * 支付宝沙箱网关
     */
    private String gatewayUrl;

    /**
     * 异步回调地址
     */
    private String notifyUrl;

    /**
     * 支付成功跳转地址
     */
    private String returnUrl;

    // 签名方式
    private String sign_type = "RSA2";
    // 字符编码格式
    private String charset = "GBK";
    // 传输格式
    private String format = "json";

}
