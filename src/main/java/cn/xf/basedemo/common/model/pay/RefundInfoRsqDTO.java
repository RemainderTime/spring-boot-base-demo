package cn.xf.basedemo.common.model.pay;

import com.wechat.pay.java.service.refund.model.Refund;
import lombok.Data;

@Data
public class RefundInfoRsqDTO {

    private boolean isSuccess;

    private String msg;

    private Refund refund;
}
