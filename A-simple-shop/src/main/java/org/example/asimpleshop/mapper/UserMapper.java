package org.example.asimpleshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.asimpleshop.pojo.User;
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
