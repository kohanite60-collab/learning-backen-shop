package org.example.asimpleshop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.asimpleshop.mapper.UserMapper;
import org.example.asimpleshop.pojo.User;
import org.example.asimpleshop.utils.PasswordHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    private UserMapper userMapper;


    public User getUserByName(String userName){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",userName);
        return userMapper.selectOne(queryWrapper);

    }


    //用户注册方法

    public boolean register(String name, String rawPassword) {
        // 1、用户名查重
        if (getUserByName(name) != null) {
            return false;
        }
        // 2、生成随机盐（每次都不同，所以两个用户密码相同，摘要也不同）
        String salt = PasswordHelper.randomSalt();
        // 3、加盐哈希
        String encrypted = PasswordHelper.encrypt(rawPassword, salt);
        // 4、盐和摘要必须成对写入，缺一不可
        User user = new User();
        user.setName(name);
        user.setPassword(encrypted);
        user.setSalt(salt);
        user.setRole("user");
        userMapper.insert(user);
        return true;
    }

}
