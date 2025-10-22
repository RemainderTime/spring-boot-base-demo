package cn.xf.basedemo.model.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * PayOrderFrom
 *
 * @author 海言
 * @date 2025/10/22
 * @time 14:09
 * @Description
 */
@Data
public class PayOrderFrom {

    @Schema(name = "商品id")
    private Long productId;

    @Schema(name = "商品名称")
    private String productName;

    @Schema(name = "商品价格")
    private BigDecimal price;

    @Schema(name = "商品数量")
    private Integer num;
}
