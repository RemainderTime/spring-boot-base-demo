package cn.xf.basedemo.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description: 消息对象
 * @ClassName: message
 * @Author: xiongfeng
 * @Date: 2023/2/21 14:26
 * @Version: 1.0
 */
@Data
@TableName("xf_message")
public class Message {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String name;

    private String content;
}
