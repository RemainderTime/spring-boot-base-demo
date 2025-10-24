package cn.xf.basedemo.mappers;

import cn.xf.basedemo.model.domain.User;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: xf-boot-base
 * @ClassName UserMapper
 * @description:
 * @author: xiongfeng
 * @create: 2022-07-04 14:42
 **/
@Mapper
@DS("master")
public interface UserMapper extends BaseMapper<User> {

}
