package org.example.asimpleshop.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.example.asimpleshop.pojo.Result;
import org.example.asimpleshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping("/userLogin")
    @ResponseBody
    public Result userLogin(String name,String pwd){


        // 1获取subject对象
        Subject subject = SecurityUtils.getSubject();
        // 2封装请求数据到token
        AuthenticationToken token = new UsernamePasswordToken(name,pwd);
        // 3调用login方法进行登录认证
        try{
            subject.login(token);
            return Result.success("登录成功");

        }catch(AuthenticationException e){
            e.printStackTrace();

            return Result.error("登录失败");
        }
}


    @PostMapping("/userRegister")
    @ResponseBody
    public Result userRegister(String name,String pwd){

        boolean ok= userService.register(name,pwd);
        if (ok ){
            return Result.success("注册成功");
        }else{
            return Result.error("注册失败");
        }




        }

        @PostMapping("/userMessage/revise")
        @RequiresRoles(value = {"admin","user"},logical = Logical.OR)
        @ResponseBody
        public Result revise(String name,String pwd){

            // 1 获取当前登录用户
            Subject subject = SecurityUtils.getSubject();
            String currentName = subject.getPrincipal().toString();

            // 2 查出当前用户的 id
            org.example.asimpleshop.pojo.User user = userService.getUserByName(currentName);
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 3 执行修改（name 为空则改名跳过，pwd 为空则改密码跳过）
            boolean ok = userService.updateUser(user.getId(), name, pwd);
            if (ok) {
                return Result.success("修改成功");
            } else {
                return Result.error("修改失败，用户名可能已被占用");
            }
        }


        @GetMapping("/userInfo")
        @RequiresRoles(value = {"admin","user"},logical = Logical.OR)
        @ResponseBody
        public Result userInfo(){

            // 1 从会话中取当前登录用户名（不是从请求参数取，防止越权查别人）
            Subject subject = SecurityUtils.getSubject();
            String currentName = subject.getPrincipal().toString();

            // 2 查库
            org.example.asimpleshop.pojo.User user = userService.getUserByName(currentName);
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 3 【安全底线】密码和盐绝不能返回给前端
            user.setPassword(null);
            user.setSalt(null);

            return Result.success(user);
        }


    @GetMapping("/userLogout")
    @ResponseBody
    public Result userLogout() {
        // 一行搞定：销毁 session、清 principals、清 rememberMe
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return Result.success("退出成功");
    }













}
