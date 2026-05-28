package cn.xf.basedemo.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String account;

    private String password;

    private String phone;

    @TableField(value = "create_time")
    private Date createTime;
}
