package cn.xf.basedemo.common.model.pay;

import lombok.Data;

@Data
public class AlipayTransRsqVo {
    private boolean success = false;
    private String code;
    private String subCode;
    private String msg;
    private String orderId;
    private String status;
    private String payFundOrderID;
    private String outBizNo;
    private String transDate;
    private String settleSerialNo;
    private String amount;
}
