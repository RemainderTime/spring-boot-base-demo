package cn.xf.basedemo.config.pay;


import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AliPayConfig
 *
 * @author 海言
 * @date 2025/10/22
 * @time 13:47
 * @Description 支付宝支付配置类
 */
@Configuration
@EnableConfigurationProperties(AliPayConfigProperties.class)
public class AliPayConfig {

    @Bean(name = "alipayClient")
    @ConditionalOnProperty(value = "pay.ali.enabled", havingValue = "true", matchIfMissing = false)
    public AlipayClient alipayClient(AliPayConfigProperties properties){
        return new DefaultAlipayClient(
                properties.getGatewayUrl(),
                properties.getAppId(),
                properties.getMerchantPrivateKey(),
                properties.getFormat(),
                properties.getCharset(),
                properties.getAlipayPublicKey(),
                properties.getSign_type());
    }
}
