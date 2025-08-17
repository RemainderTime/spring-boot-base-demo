package cn.xf.basedemo.common.model.pay;

import lombok.Data;

@Data
public class WxPayTransRsqVo {
    private boolean success = false;
    private String code;
    private String msg;

}
