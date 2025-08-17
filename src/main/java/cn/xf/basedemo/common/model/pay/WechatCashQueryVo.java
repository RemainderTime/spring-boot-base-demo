package cn.xf.basedemo.common.model.pay;


import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class WechatCashQueryVo {
    private String appid;
    private String batch_id;
    private String detail_id;
    private String detail_status;
    private OffsetDateTime initiate_time;
    private String mchid;
    private String openid;
    private String out_batch_no;
    private String out_detail_no;
    private long transfer_amount;
    private String transfer_remark;
    private OffsetDateTime update_time;
    private String user_name;
}

