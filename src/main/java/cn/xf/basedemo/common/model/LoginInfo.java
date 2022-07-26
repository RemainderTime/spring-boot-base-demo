package cn.xf.basedemo.common.model;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Data;

/**
 * @program: xf-boot-base
 * @ClassName LoginInfo
 * @description:
 * @author: xiongfeng
 * @create: 2022-07-04 14:26
 **/
@Data
public class LoginInfo {

    private String account;

    private String pwd;

    public String check(){
        if(StringUtils.isEmpty(account)){
            return "账号不能为空";
        }
        if(StringUtils.isEmpty(pwd)){
            return "密码不能为空";
        }
        return "";
    }
}
