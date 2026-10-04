package org.example.asimpleshop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.asimpleshop.mapper.UserMapper;
import org.example.asimpleshop.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    private UserMapper userMapper;
    public User getUserByName(String userName){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userName);
        return userMapper.selectOne(queryWrapper);

    }
}
