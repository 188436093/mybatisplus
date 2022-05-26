package com.maxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maxy.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定义的泛型指定了该mapper操作哪个数据库中的表
 * @author maxy
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
