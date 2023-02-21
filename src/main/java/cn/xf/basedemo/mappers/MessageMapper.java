package cn.xf.basedemo.mappers;

import cn.xf.basedemo.model.domain.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: 消息mapper
 * @ClassName: MessageMapper
 * @Author: xiongfeng
 * @Date: 2023/2/21 14:28
 * @Version: 1.0
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}
