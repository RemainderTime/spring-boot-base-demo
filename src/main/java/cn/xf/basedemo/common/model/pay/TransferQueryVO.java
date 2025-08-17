package cn.xf.basedemo.common.model.pay;

import lombok.Data;

@Data
public class TransferQueryVO {

    private boolean isSuccess;
    private String resCode;
    private String resCodeDes;
    private WechatCashQueryVo cashQueryVo;
}
