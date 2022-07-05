package cn.xf.basedemo.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @program: xf-boot-base
 * @ClassName User
 * @description:
 * @author: xiongfeng
 * @create: 2022-07-04 14:39
 **/
@Data
@TableName(value = "xf_user")
public class User {

    private Integer id;

    private String name;

    private String account;

    private String password;

    private String phone;

    private Date createTime;
}
