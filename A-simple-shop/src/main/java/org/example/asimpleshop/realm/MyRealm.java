package org.example.asimpleshop.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.lang.util.ByteSource;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import org.example.asimpleshop.mapper.UserMapper;
import org.example.asimpleshop.pojo.User;
import org.example.asimpleshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        // 1 获取用户信息
        String name = principalCollection.getPrimaryPrincipal().toString();
        // 2 调用接口请求数据库，获取用户角色信息，这里省略
        // 2.5 调用接口请求数据库，获取角色权限信息
        // 3 创建对象，封装当前登录用户的角色、权限信息
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();


        User user = userService.getUserByName(name);


        info.addRole(user.getRole());
//        info.addStringPermission("user:add"); // 模拟该角色的权限有 user:add
        // 4 返回信息
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 1、获取用户信息
        String name = authenticationToken.getPrincipal().toString();
        // 2、获取数据库用户信息
        User user = userService.getUserByName(name);
        // 3、判断，将数据封装返回
        if (user != null){
            AuthenticationInfo info = new SimpleAuthenticationInfo(
                    authenticationToken.getPrincipal(),
                    user.getPassword(),
                    ByteSource.Util.bytes(user.getSalt()),
                    authenticationToken.getPrincipal().toString()
            );

            return info;
        }
        return null;
    }

}
