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


    // 按 id 查询用户（用于查看/修改信息）
    public User getUserById(int id) {
        return userMapper.selectById(id);
    }


    /**
     * 修改用户信息
     * @param id      用户 id
     * @param name    新用户名（为空则不修改）
     * @param rawPwd  新明文密码（为空则不修改密码）
     * @return 是否修改成功
     */
    public boolean updateUser(int id, String name, String rawPwd) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return false;
        }
        // 1、改名（非空才改，且要查重）
        if (name != null && !name.isEmpty()) {
            User same = getUserByName(name);
            if (same != null && same.getId() != id) {
                return false;   // 用户名被其他人占用
            }
            user.setName(name);
        }
        // 2、改密码：重新生成随机盐 + 加盐哈希，盐和摘要必须成对更新
        if (rawPwd != null && !rawPwd.isEmpty()) {
            String salt = PasswordHelper.randomSalt();
            user.setSalt(salt);
            user.setPassword(PasswordHelper.encrypt(rawPwd, salt));
        }
        // 3、写回
        return userMapper.updateById(user) > 0;
    }

}
